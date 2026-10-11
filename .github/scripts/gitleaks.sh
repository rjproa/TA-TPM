#!/usr/bin/env bash
# Descarga una versión fija de Gitleaks, comprueba su integridad y revisa todo el historial.
set -euo pipefail

VERSION="8.30.1"
ARCHIVO="gitleaks_${VERSION}_linux_x64.tar.gz"
BASE="https://github.com/gitleaks/gitleaks/releases/download/v${VERSION}"
DESTINO="${RUNNER_TEMP:-/tmp}/gitleaks"

mkdir -p "$DESTINO"
curl -sSfL -o "$DESTINO/$ARCHIVO" "$BASE/$ARCHIVO"
curl -sSfL -o "$DESTINO/checksums.txt" "$BASE/gitleaks_${VERSION}_checksums.txt"
(cd "$DESTINO" && grep " $ARCHIVO\$" checksums.txt | sha256sum -c -)
tar -xzf "$DESTINO/$ARCHIVO" -C "$DESTINO" gitleaks

"$DESTINO/gitleaks" git --config .gitleaks.toml --redact --no-banner --verbose .
