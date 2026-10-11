#!/usr/bin/env bash
# Falla si el análisis de CodeQL contiene hallazgos de severidad alta o crítica.
# Uso: revisar-sarif.sh <carpeta con archivos .sarif>
set -euo pipefail

CARPETA="${1:?Falta la carpeta de resultados}"
UMBRAL="7.0"

FILTRO='
  [ .runs[] as $run
    | $run.results[]
    | . as $resultado
    | ( if ($resultado.rule.toolComponent.index // null) != null
        then $run.tool.extensions[$resultado.rule.toolComponent.index].rules
        else $run.tool.driver.rules
        end ) as $reglas
    | $reglas[($resultado.ruleIndex // $resultado.rule.index)] as $regla
    | (($regla.properties["security-severity"] // "0") | tonumber) as $severidad
    | select($severidad >= ($umbral | tonumber))
    | { regla: $regla.id,
        severidad: $severidad,
        archivo: $resultado.locations[0].physicalLocation.artifactLocation.uri,
        linea: $resultado.locations[0].physicalLocation.region.startLine,
        mensaje: $resultado.message.text }
  ]'

GRAVES=$(jq -s --arg umbral "$UMBRAL" "[ .[] | $FILTRO ] | add // []" "$CARPETA"/*.sarif)
TOTAL=$(echo "$GRAVES" | jq 'length')

if [ "$TOTAL" -gt 0 ]; then
  echo "$GRAVES" | jq -r '.[] | "::error file=\(.archivo),line=\(.linea)::[\(.regla)] severidad \(.severidad): \(.mensaje)"'
  echo "CodeQL encontró $TOTAL hallazgo(s) de severidad $UMBRAL o superior. No se publica el instalador."
  exit 1
fi
echo "CodeQL no encontró hallazgos de severidad $UMBRAL o superior."
