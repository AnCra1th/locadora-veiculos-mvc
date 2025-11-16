# Projeto Locadora de Veículos
 Trabalho para composição de nota na disciplina Programação Orientada a Objetos II do semestre 2025.2.   

## Requisitos da aplicação em Java
- Palavra reservada final
- Palavra reservada static
- Framework Collections
- Tratamento de Exceções
- Interface (tipo de classe)
- Interface gráfica (swing)
- Operações com banco de dados (incliusão, exclusão, elteração e consulta.). O Banco de Dados a ser utilizado será  o **MySQL**;


### Datas importantes
- NUP02 -> data possíveis: 17/11; 24/11; 01/12


### Requisitos importantes para o programa funcionar devidamente durante a apresentação:
- O banco de dados, ao ser criado como novo, usando o docker compose, está sem dados. **É necessário colocar as categorias do veículo na tabela "categoria_veiculos" e cadastrar ao menos 1 (um) funcionário na tabela "funcionario"**.

- Rodar **"docker compose down"** e **"docker compose down -v"** para excluir o contâiner e volume do contâiner. Após isso, rodar o seguinte comando para criar e subir o contâiner com o banco de dados **"docker compose up -d --build"** (é necessário estar no mesmo diretório do arquivo docker-compose.yml).
- Para criar o contâiner docker com o banco de dados, basta rodar o seguinte comando no diretório raiz do projeto: **docker compose up** (É importante possuir o docker instalado e de que as portas 3306:3306 estão liberadas para o container rodar!).
