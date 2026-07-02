package com.seriestv;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private Usuario usuario;
    private PersistenciaJSON persistencia;
    private ApiTVMaze api;

    private JTextField campoBusca;
    private DefaultTableModel modeloBusca;
    private JTable tabelaBusca;
    private List<Serie> seriesEncontradas;

    private ListaPanel listaPanel;

    private CardLayout cardLayout;
    private JPanel painelConteudo;

    public MainFrame(Usuario usuario, PersistenciaJSON persistencia) {
        this.usuario = usuario;
        this.persistencia = persistencia;
        this.api = new ApiTVMaze();

        setTitle("SeriesTV - " + usuario.getNome());
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                salvarESair();
            }
        });

        criarTela();
    }

    private void criarTela() {

        // BOTÕES
        JButton btnBusca = new JButton("Buscar Series");
        JButton btnListas = new JButton("Minhas Listas");
        JButton btnLogout = new JButton("Sair");

        // TOPO ORGANIZADO (CORRIGE BOTÃO SUMINDO)
        JPanel topo = new JPanel(new BorderLayout());

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        esquerda.add(new JLabel("Ola, " + usuario.getNome() + "!"));
        esquerda.add(btnBusca);
        esquerda.add(btnListas);

        direita.add(btnLogout);

        topo.add(esquerda, BorderLayout.WEST);
        topo.add(direita, BorderLayout.EAST);

        add(topo, BorderLayout.NORTH);

        // LOGOUT
        btnLogout.addActionListener(e -> logout());

        // TELAS
        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        painelConteudo.add(criarPainelBusca(), "busca");

        listaPanel = new ListaPanel(usuario, persistencia);
        painelConteudo.add(listaPanel, "listas");

        add(painelConteudo, BorderLayout.CENTER);

        // AÇÕES
        btnBusca.addActionListener(e -> cardLayout.show(painelConteudo, "busca"));
        btnListas.addActionListener(e -> {
            listaPanel.atualizar();
            cardLayout.show(painelConteudo, "listas");
        });
    }

    private void logout() {
        try {
            persistencia.salvar(usuario);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }

        this.dispose();
        new LoginFrame(persistencia).setVisible(true);
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        campoBusca = new JTextField();
        JButton btnBuscar = new JButton("Buscar");

        JPanel topoBusca = new JPanel(new BorderLayout(5, 0));
        topoBusca.add(new JLabel("Nome da serie: "), BorderLayout.WEST);
        topoBusca.add(campoBusca, BorderLayout.CENTER);
        topoBusca.add(btnBuscar, BorderLayout.EAST);

        painel.add(topoBusca, BorderLayout.NORTH);

        String[] colunas = {"Nome", "Idioma", "Generos", "Nota", "Status", "Estreia", "Emissora"};

        modeloBusca = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabelaBusca = new JTable(modeloBusca);
        tabelaBusca.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        painel.add(new JScrollPane(tabelaBusca), BorderLayout.CENTER);

        JButton btnFavorito = new JButton("+ Favoritos");
        JButton btnAssistida = new JButton("+ Ja Assisti");
        JButton btnQuero = new JButton("+ Quero Assistir");

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rodape.add(new JLabel("Adicionar a lista: "));
        rodape.add(btnFavorito);
        rodape.add(btnAssistida);
        rodape.add(btnQuero);

        painel.add(rodape, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscar());
        campoBusca.addActionListener(e -> buscar());

        btnFavorito.addActionListener(e -> adicionarNaLista("favorito"));
        btnAssistida.addActionListener(e -> adicionarNaLista("assistida"));
        btnQuero.addActionListener(e -> adicionarNaLista("quero"));

        return painel;
    }

    private void buscar() {
        String termo = campoBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome de uma serie.");
            return;
        }

        modeloBusca.setRowCount(0);

        SwingWorker<List<Serie>, Void> worker = new SwingWorker<>() {
            protected List<Serie> doInBackground() throws Exception {
                return api.buscarSeries(termo);
            }

            protected void done() {
                try {
                    seriesEncontradas = get();

                    if (seriesEncontradas.isEmpty()) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Nenhuma serie encontrada.");
                        return;
                    }

                    for (Serie s : seriesEncontradas) {
                        modeloBusca.addRow(new Object[]{
                                s.getName(),
                                s.getLanguage(),
                                s.getGenresFormatted(),
                                s.getRating() > 0 ? s.getRating() : "-",
                                s.getStatus(),
                                s.getPremiered(),
                                s.getNetwork()
                        });
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Erro ao buscar: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    private void adicionarNaLista(String lista) {
        int linha = tabelaBusca.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma serie primeiro.");
            return;
        }

        Serie serie = seriesEncontradas.get(linha);

        switch (lista) {
            case "favorito" -> usuario.adicionarFavorito(serie);
            case "assistida" -> usuario.adicionarJaAssistida(serie);
            case "quero" -> usuario.adicionarQueroAssistir(serie);
        }

        try {
            persistencia.salvar(usuario);
            JOptionPane.showMessageDialog(this, "\"" + serie.getName() + "\" adicionada!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void salvarESair() {
        try {
            persistencia.salvar(usuario);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
        System.exit(0);
    }
}