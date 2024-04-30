#!/bin/bash

# Uso: ./tempo_execucao.sh <tempo_limite_em_segundos>

tempo_limite=$1

# Inicia um loop que dura o tempo especificado
end=$((SECONDS+tempo_limite))

while [ $SECONDS -lt $end ]; do
    # O comando 'sleep 1' é usado para evitar uso excessivo da CPU
    sleep 1
done

echo "Tempo de execução de $tempo_limite segundos concluído."
