package com.locadoraveiculos;

// Imports de componentes do sistema
import com.locadoraveiculos.config.DatabaseConnection;
import com.locadoraveiculos.controller.ClienteController;
import com.locadoraveiculos.controller.LocacaoController;
import com.locadoraveiculos.controller.VeiculoController;
import com.locadoraveiculos.controller.CategoriaVeiculoController;
import com.locadoraveiculos.controller.ReservaController;
import com.locadoraveiculos.model.Cliente;
import com.locadoraveiculos.model.Locacao;
import com.locadoraveiculos.model.Veiculo;
import com.locadoraveiculos.model.CategoriaVeiculo;
import com.locadoraveiculos.model.Reserva;

// Imports de utilitários Java
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

/**
 * Classe principal para interação com o sistema de locadora via console.
 * Atua como a camada de "View" (Visão) da aplicação.
 */
public class Main {
    // Instanciação de todos os controllers necessários para a aplicação
    private static ClienteController clienteController = new ClienteController();
    private static VeiculoController veiculoController = new VeiculoController();
    private static LocacaoController locacaoController = new LocacaoController();
    private static CategoriaVeiculoController categoriaVeiculoController = new CategoriaVeiculoController();
    private static ReservaController reservaController = new ReservaController();

    // Utilitários para entrada de dados e formatação de datas
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");


    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Sistema de Locadora de Veículos!");
        try {
            // Tenta estabelecer a conexão com o banco de dados na inicialização
            DatabaseConnection.getConnection();
            System.out.println("INFO: Conexão com o banco de dados estabelecida.");
        } catch (SQLException e) {
            System.err.println("FALHA CRÍTICA: Não foi possível conectar ao banco de dados. Verifique as configurações e o servidor MySQL.");
            System.err.println("Detalhes do erro: " + e.getMessage());
            System.err.println("A aplicação será encerrada.");
            return; // Encerra a aplicação se não conseguir conectar
        }

        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerOpcaoInteira("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuVeiculos();
                    break;
                case 3:
                    menuReservas(); // Nova opção
                    break;
                case 4:
                    menuLocacoes(); // Ordem ajustada
                    break;
                case 0:
                    System.out.println("Encerrando o Sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            if (opcao != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine(); // Pausa para o usuário ler a saída antes de mostrar o menu novamente
            }
        } while (opcao != 0);

        scanner.close();
        DatabaseConnection.closeConnection();
        System.out.println("INFO: Conexão com o banco de dados fechada. Sistema encerrado.");
    }

    // --- MÉTODOS DE UTILIDADE PARA ENTRADA DE DADOS ---

    private static String lerString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int lerOpcaoInteira(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
            }
        }
    }

    private static BigDecimal lerBigDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) return null; // Permite entrada vazia para campos opcionais
                return new BigDecimal(input.replace(",", ".")); // Aceita vírgula como separador decimal
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número decimal (ex: 150.75).");
            }
        }
    }

    private static Date lerData(String prompt, SimpleDateFormat sdf) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) return null; // Permite entrada vazia para campos opcionais
            try {
                return sdf.parse(input);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Use o formato " + sdf.toPattern() + ".");
            }
        }
    }

    // --- MENUS PRINCIPAIS E SUB-MENUS ---

    private static void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Gerenciar Clientes");
        System.out.println("2. Gerenciar Veículos");
        System.out.println("3. Gerenciar Reservas");
        System.out.println("4. Gerenciar Locações");
        System.out.println("0. Sair");
    }

    // --- SEÇÃO DE CLIENTES ---
    private static void menuClientes() {
        int opcao;
        do {
            System.out.println("\n--- Gerenciar Clientes ---");
            System.out.println("1. Cadastrar Novo Cliente");
            System.out.println("2. Listar Todos os Clientes");
            System.out.println("3. Buscar Cliente por ID");
            System.out.println("4. Atualizar Cliente");
            System.out.println("5. Excluir Cliente");
            System.out.println("0. Voltar ao Menu Principal");
            opcao = lerOpcaoInteira("Escolha uma opção: ");
            switch (opcao) {
                case 1: cadastrarCliente(); break;
                case 2: listarClientes(); break;
                case 3: buscarCliente(); break;
                case 4: atualizarCliente(); break;
                case 5: excluirCliente(); break;
            }
        } while (opcao != 0);
    }

    private static void cadastrarCliente() {
        System.out.println("\n--- Cadastrar Novo Cliente ---");
        Cliente cliente = new Cliente();
        cliente.setNome(lerString("Nome completo: "));
        cliente.setCpf(lerString("CPF (somente números): "));
        cliente.setCnh(lerString("CNH: "));
        cliente.setEmail(lerString("Email: "));
        cliente.setTelefone(lerString("Telefone (opcional): "));
        cliente.setDataValidadeCnh(lerData("Data de Validade CNH (dd/MM/yyyy, opcional): ", dateFormat));
        cliente.setEnderecoRua(lerString("Endereço - Rua (opcional): "));
        cliente.setEnderecoNumero(lerString("Endereço - Número (opcional): "));
        clienteController.cadastrarCliente(cliente);
    }

    private static void listarClientes() {
        System.out.println("\n--- Lista de Clientes ---");
        List<Cliente> clientes = clienteController.listarTodosClientes();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            clientes.forEach(System.out::println);
        }
    }

    private static void buscarCliente() {
        System.out.println("\n--- Buscar Cliente por ID ---");
        int id = lerOpcaoInteira("ID do Cliente: ");
        Cliente cliente = clienteController.buscarClientePorId(id);
        if (cliente != null) {
            System.out.println("Cliente encontrado: \n" + cliente);
        } else {
            System.out.println("Cliente com ID " + id + " não encontrado.");
        }
    }

    private static void atualizarCliente() {
        System.out.println("\n--- Atualizar Cliente ---");
        int id = lerOpcaoInteira("ID do Cliente a ser atualizado: ");
        Cliente cliente = clienteController.buscarClientePorId(id);
        if (cliente == null) {
            System.out.println("Cliente com ID " + id + " não encontrado.");
            return;
        }
        System.out.println("Dados atuais: " + cliente);
        System.out.println("Deixe o campo em branco para não alterar o valor atual.");
        String nome = lerString("Novo Nome (" + cliente.getNome() + "): ");
        if (!nome.trim().isEmpty()) cliente.setNome(nome);
        String email = lerString("Novo Email (" + cliente.getEmail() + "): ");
        if (!email.trim().isEmpty()) cliente.setEmail(email);
        clienteController.atualizarCliente(cliente);
    }

    private static void excluirCliente() {
        System.out.println("\n--- Excluir Cliente ---");
        int id = lerOpcaoInteira("ID do Cliente a ser excluído: ");
        clienteController.excluirCliente(id);
    }

    // --- SEÇÃO DE VEÍCULOS ---
    private static void menuVeiculos() {
        int opcao;
        do {
            System.out.println("\n--- Gerenciar Veículos ---");
            System.out.println("1. Cadastrar Novo Veículo");
            System.out.println("2. Listar Todos os Veículos");
            System.out.println("3. Buscar Veículo por Placa");
            System.out.println("4. Atualizar Veículo");
            System.out.println("5. Excluir Veículo");
            System.out.println("0. Voltar ao Menu Principal");
            opcao = lerOpcaoInteira("Escolha uma opção: ");
            switch (opcao) {
                case 1: cadastrarVeiculo(); break;
                case 2: listarVeiculos(); break;
                case 3: buscarVeiculo(); break;
                case 4: atualizarVeiculo(); break;
                case 5: excluirVeiculo(); break;
            }
        } while (opcao != 0);
    }

    private static void cadastrarVeiculo() {
        System.out.println("\n--- Cadastrar Novo Veículo ---");
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(lerString("Placa (ex: BRA2E19): "));
        listarCategorias();
        veiculo.setIdCategoriaVeiculo(lerOpcaoInteira("ID da Categoria do Veículo: "));
        veiculo.setModelo(lerString("Modelo: "));
        veiculo.setMarca(lerString("Marca: "));
        String anoStr = lerString("Ano de Fabricação (opcional): ");
        if (!anoStr.trim().isEmpty()) {
            try {
                veiculo.setAnoFabricacao(Integer.parseInt(anoStr));
            } catch (NumberFormatException e) {
                System.out.println("Ano de fabricação inválido, será deixado em branco.");
            }
        }
        veiculo.setCor(lerString("Cor (opcional): "));
        veiculo.setChassi(lerString("Chassi (opcional, 17 caracteres): "));
        veiculo.setRenavam(lerString("Renavam (opcional, 11 caracteres): "));
        veiculo.setStatusVeiculo(lerString("Status Inicial (disponivel/manutencao/etc.): "));
        veiculo.setObservacoes(lerString("Observações (opcional): "));
        veiculoController.cadastrarVeiculo(veiculo);
    }

    private static void listarVeiculos() {
        System.out.println("\n--- Lista de Veículos ---");
        List<Veiculo> veiculos = veiculoController.listarTodosVeiculos();
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo cadastrado.");
        } else {
            veiculos.forEach(System.out::println);
        }
    }

    private static void buscarVeiculo() {
        System.out.println("\n--- Buscar Veículo por Placa ---");
        String placa = lerString("Placa do Veículo: ");
        Veiculo veiculo = veiculoController.buscarVeiculoPorPlaca(placa);
        if (veiculo != null) {
            System.out.println("Veículo encontrado: " + veiculo);
        } else {
            System.out.println("Veículo com placa " + placa + " não encontrado.");
        }
    }

    private static void atualizarVeiculo() {
        System.out.println("\n--- Atualizar Veículo ---");
        String placa = lerString("Placa do Veículo a ser atualizado: ");
        Veiculo veiculo = veiculoController.buscarVeiculoPorPlaca(placa);
        if (veiculo == null) {
            System.out.println("Veículo com placa " + placa + " não encontrado.");
            return;
        }
        System.out.println("Dados atuais: " + veiculo);
        System.out.println("Deixe o campo em branco para não alterar o valor atual.");
        String modelo = lerString("Novo Modelo (" + veiculo.getModelo() + "): ");
        if (!modelo.trim().isEmpty()) veiculo.setModelo(modelo);
        String status = lerString("Novo Status (" + veiculo.getStatusVeiculo() + "): ");
        if (!status.trim().isEmpty()) veiculo.setStatusVeiculo(status.toLowerCase());
        veiculoController.atualizarVeiculo(veiculo);
    }

    private static void excluirVeiculo() {
        System.out.println("\n--- Excluir Veículo ---");
        String placa = lerString("Placa do Veículo a ser excluído: ");
        veiculoController.excluirVeiculo(placa);
    }

    // --- SEÇÃO DE RESERVAS ---
    private static void menuReservas() {
        int opcao;
        do {
            System.out.println("\n--- Gerenciar Reservas ---");
            System.out.println("1. Criar Nova Reserva");
            System.out.println("2. Listar Todas as Reservas");
            System.out.println("3. Buscar Reserva por ID");
            System.out.println("4. Cancelar Reserva");
            System.out.println("0. Voltar ao Menu Principal");
            opcao = lerOpcaoInteira("Escolha uma opção: ");
            switch (opcao) {
                case 1: criarReserva(); break;
                case 2: listarReservas(); break;
                case 3: buscarReserva(); break;
                case 4: cancelarReserva(); break;
            }
        } while (opcao != 0);
    }
    
    private static void listarCategorias() {
        System.out.println("\n--- Categorias de Veículo Disponíveis ---");
        List<CategoriaVeiculo> categorias = categoriaVeiculoController.listarTodasCategorias();
        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada no sistema.");
        } else {
            for (CategoriaVeiculo categoria : categorias) {
                System.out.println("ID: " + categoria.getIdCategoriaVeiculo() + " - " + categoria.getNomeCategoria() + " (Diária base: R$" + categoria.getValorDiariaBase() + ")");
            }
        }
    }

    private static void criarReserva() {
        System.out.println("\n--- Criar Nova Reserva ---");
        listarCategorias();
        if (categoriaVeiculoController.listarTodasCategorias().isEmpty()) {
            System.out.println("Não é possível criar uma reserva pois não há categorias de veículo cadastradas.");
            return;
        }
        Reserva reserva = new Reserva();
        reserva.setIdCliente(lerOpcaoInteira("ID do Cliente: "));
        reserva.setIdCategoriaVeiculo(lerOpcaoInteira("ID da Categoria do Veículo desejada: "));
        int idFuncionario = lerOpcaoInteira("ID do Funcionário que está registrando (opcional, 0 para nenhum): ");
        if (idFuncionario != 0) {
            reserva.setIdFuncionario(idFuncionario);
        }
        reserva.setDataPrevistaRetirada(lerData("Data e Hora Prevista de Retirada (dd/MM/yyyy HH:mm): ", dateTimeFormat));
        if (reserva.getDataPrevistaRetirada() == null) { System.out.println("Data de retirada é obrigatória."); return; }
        reserva.setDataPrevistaDevolucao(lerData("Data e Hora Prevista de Devolução (dd/MM/yyyy HH:mm): ", dateTimeFormat));
        if (reserva.getDataPrevistaDevolucao() == null) { System.out.println("Data prevista de devolução é obrigatória."); return; }
        reserva.setValorSinalReserva(lerBigDecimal("Valor do Sinal da Reserva (opcional, deixe em branco para 0.00): "));
        reserva.setObservacoes(lerString("Observações (opcional): "));
        reservaController.criarReserva(reserva);
    }

    private static void listarReservas() {
        System.out.println("\n--- Lista de Reservas ---");
        List<Reserva> reservas = reservaController.listarTodasReservas();
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva encontrada.");
        } else {
            reservas.forEach(System.out::println);
        }
    }

    private static void buscarReserva() {
        System.out.println("\n--- Buscar Reserva por ID ---");
        int id = lerOpcaoInteira("ID da Reserva: ");
        Reserva reserva = reservaController.buscarReservaPorId(id);
        if (reserva != null) {
            System.out.println("Reserva encontrada: " + reserva);
        } else {
            System.out.println("Reserva com ID " + id + " não encontrada.");
        }
    }

    private static void cancelarReserva() {
        System.out.println("\n--- Cancelar Reserva ---");
        int id = lerOpcaoInteira("ID da Reserva a ser cancelada: ");
        reservaController.cancelarReserva(id);
    }

    // --- SEÇÃO DE LOCAÇÕES ---
    private static void menuLocacoes() {
        int opcao;
        do {
            System.out.println("\n--- Gerenciar Locações ---");
            System.out.println("1. Registrar Nova Locação");
            System.out.println("2. Listar Todas as Locações");
            System.out.println("3. Buscar Locação por ID");
            System.out.println("4. Finalizar Locação");
            System.out.println("0. Voltar ao Menu Principal");
            opcao = lerOpcaoInteira("Escolha uma opção: ");
            switch (opcao) {
                case 1: registrarLocacao(); break;
                case 2: listarLocacoes(); break;
                case 3: buscarLocacao(); break;
                case 4: finalizarLocacao(); break;
            }
        } while (opcao != 0);
    }

    private static void registrarLocacao() {
        System.out.println("\n--- Registrar Nova Locação ---");
        int idCliente = lerOpcaoInteira("ID do Cliente: ");
        String placaVeiculo = lerString("Placa do Veículo: ").toUpperCase();
        int idFuncionarioRetirada = lerOpcaoInteira("ID do Funcionário (Retirada): ");
        Date dataRetirada = lerData("Data e Hora de Retirada (dd/MM/yyyy HH:mm): ", dateTimeFormat);
        if (dataRetirada == null) { System.out.println("Data de retirada é obrigatória."); return; }
        Date dataPrevistaDevolucao = lerData("Data e Hora Prevista de Devolução (dd/MM/yyyy HH:mm): ", dateTimeFormat);
        if (dataPrevistaDevolucao == null) { System.out.println("Data prevista de devolução é obrigatória."); return; }
        BigDecimal valorDiaria = lerBigDecimal("Valor da Diária da Locação: ");
        if (valorDiaria == null || valorDiaria.compareTo(BigDecimal.ZERO) <= 0) { System.out.println("Valor da diária é obrigatório e deve ser positivo."); return;}
        BigDecimal valorCaucao = lerBigDecimal("Valor do Caução (opcional, deixe em branco para 0.00): ");
        BigDecimal valorSeguro = lerBigDecimal("Valor do Seguro (opcional, deixe em branco para 0.00): ");
        String obsRetirada = lerString("Observações da Retirada (opcional): ");
        Locacao locacao = new Locacao(idCliente, placaVeiculo, idFuncionarioRetirada, dataRetirada, dataPrevistaDevolucao, valorDiaria);
        if(valorCaucao != null) locacao.setValorCaucao(valorCaucao);
        if(valorSeguro != null) locacao.setValorSeguro(valorSeguro);
        locacao.setObservacoesRetirada(obsRetirada);
        locacaoController.registrarLocacao(locacao);
    }

    private static void listarLocacoes() {
        System.out.println("\n--- Lista de Locações ---");
        List<Locacao> locacoes = locacaoController.listarTodasLocacoes();
        if (locacoes.isEmpty()) {
            System.out.println("Nenhuma locação registrada.");
        } else {
            locacoes.forEach(System.out::println);
        }
    }

    private static void buscarLocacao() {
        System.out.println("\n--- Buscar Locação por ID ---");
        int id = lerOpcaoInteira("ID da Locação: ");
        Locacao locacao = locacaoController.buscarLocacaoPorId(id);
        if (locacao != null) {
            System.out.println("Locação encontrada: " + locacao);
        } else {
            System.out.println("Locação com ID " + id + " não encontrada.");
        }
    }

    private static void finalizarLocacao() {
        System.out.println("\n--- Finalizar Locação ---");
        int idLocacao = lerOpcaoInteira("ID da Locação a ser finalizada: ");
        Date dataEfetivaDevolucao = lerData("Data e Hora Efetiva de Devolução (dd/MM/yyyy HH:mm): ", dateTimeFormat);
        if (dataEfetivaDevolucao == null) { System.out.println("Data efetiva de devolução é obrigatória."); return; }
        String obsDevolucao = lerString("Observações da Devolução (avarias, km, etc. - opcional): ");
        locacaoController.finalizarLocacao(idLocacao, dataEfetivaDevolucao, obsDevolucao);
    }
}
