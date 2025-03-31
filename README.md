# Chunk Uploader

Arquitetura de Front e Back para envio e recepção de arquivos grandes.

## Motivação

Chrome e navegadores baseados em Chromium tendem a carregar todo arquivo para a memória. Para resolver esse problema, foi criado um programa que faz a leitura do arquivo em chunks (partes menores) e os envia para o backend. O backend, por sua vez, recebe os chunks e os grava em lotes. Após todos os pacotes serem enviados, o frontend notifica que o envio foi concluído, e o backend monta novamente o arquivo original a partir dos chunks recebidos.
    
