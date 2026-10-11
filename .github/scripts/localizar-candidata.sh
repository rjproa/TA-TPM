#!/usr/bin/env bash
# Busca la versión candidata cuyo código es idéntico al de la rama actual.
# Compara el árbol de archivos de Git, así que no importa cómo se hizo la fusión.
set -euo pipefail

ARBOL=$(git rev-parse 'HEAD^{tree}')
CANDIDATA=""
for etiqueta in $(git tag --list 'v*-rc.*' --sort=-creatordate); do
  if [ "$(git rev-parse "${etiqueta}^{tree}")" = "$ARBOL" ]; then
    CANDIDATA="$etiqueta"
    break
  fi
done

if [ -z "$CANDIDATA" ]; then
  echo "::error::No hay ninguna versión candidata con el mismo código que esta rama."
  echo "Causas habituales:"
  echo "  - El workflow cert todavía no terminó en develop: espera y vuelve a ejecutar este workflow."
  echo "  - main tiene cambios que no pasaron por develop: todo cambio debe certificarse primero."
  exit 1
fi

VERSION="${CANDIDATA#v}"
VERSION="${VERSION%%-rc.*}"

if gh release view "v$VERSION" >/dev/null 2>&1; then
  echo "::error::La versión $VERSION ya es oficial. Sube yunta.versionName en gradle.properties y certifica de nuevo."
  exit 1
fi

echo "Candidata: $CANDIDATA  ->  versión oficial: v$VERSION"
{
  echo "candidata=$CANDIDATA"
  echo "version=$VERSION"
} >> "$GITHUB_OUTPUT"
