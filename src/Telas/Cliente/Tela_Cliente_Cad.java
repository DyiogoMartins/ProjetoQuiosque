package Telas.Cliente;

import Funcoes.Verifica_Validacao;
import java.sql.*;
// chama o arquivo de conexao com o banco de dados
import Connection.ConnectionClass;
import Funcoes.Funcoes_Sql;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author DIOGO e eric
 */

public class Tela_Cliente_Cad extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    boolean cepEncontrado = false;
    boolean cpfEncontrado = false;
    boolean telEncontrado = false;
    boolean emailEncontrado = false;
    int idCli = 0;

    public Tela_Cliente_Cad() {
        initComponents();
        conexao = ConnectionClass.conector();
        consultaCliente();
        procedAjustaTable();

    }

    private void procedAjustaTable() {

        //tblFunc.setGridColor(Color.RGBtoHSB(ERROR, WIDTH, ABORT, "0F0F0F"));
        tblCli.setShowGrid(true);
        tblCli.setRowHeight(30);

        //Define o modo de redimensionamento automático da tabela como 
        //"AUTO_RESIZE_OFF", que desabilita o redimensionamento automático da
        //largura das colunas.
        tblCli.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < tblCli.getColumnCount(); column++) {
            TableColumn ColunaTable = tblCli.getColumnModel().getColumn(column);
            // largura preferida da coluna(mínima)
            int miniWidth = ColunaTable.getMinWidth();
            // largura máxima da coluna.
            int maxWidth = ColunaTable.getMaxWidth();

            // Percorre cada linha da tabela para obter o conteúdo da célula.
            for (int linhas = 0; linhas < tblCli.getRowCount(); linhas++) {
                // Obtém o renderizador(o valor ) de célula para a célula especificada.
                TableCellRenderer renderizadorCelula = tblCli.getCellRenderer(linhas, column);
                // Prepara o renderizador para desenhar na célula especificada.
                Component c = tblCli.prepareRenderer(renderizadorCelula, linhas, column);
                // Obtém a largura preferida do componente que representa a célula.
                int width = c.getPreferredSize().width + tblCli.getIntercellSpacing().width;
                // Calcula a largura total da célula, que é a largura preferida (minima)
                //  + o espaçamento entre as células.
                miniWidth = Math.max(miniWidth, width);

                // Se a largura preferida(minima) da coluna for maior ou igual à largura 
                // máxima da coluna, define a largura preferida(minima) da coluna 
                // como a largura máxima da coluna.
                if (miniWidth >= maxWidth) {
                    miniWidth = maxWidth;
                    break;
                }
            }

            // Define a largura preferida(minima) da coluna como a largura da coluna atual.
            ColunaTable.setPreferredWidth(miniWidth + (15 * tblCli.getColumnName(column).length()));
            //ColunaTable.setPreferredWidth(preferredWidth + ColunaTable.getWidth());
        }

    }

    private void consultaCliente() {
        //OBS: TIRAR ESSE COMANDO SQL DA TELA E POR EM UMA CLASSE SEPARADA POR SEGURANCA

        String sql = "select NomeCli as Nome, CPF, telefone \n"
                + ", email as Email, CEP, endereco as endereço,"
                + " cidade as Cidade, bairro as Bairro, uf as UF,"
                + " Obs from tb_cliente where NomeCli like ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisar.getText() + "%");
            rs = pst.executeQuery();

            tblCli.setModel(DbUtils.resultSetToTableModel(rs));
            procedAjustaTable();

        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void cadastrar() {
        String sql = "insert into tb_cliente(NomeCli, CPF, \n"
                + "telefone, email, CEP, endereco, cidade, bairro, UF, Obs) \n"
                + "values(?,?,?,?,?,?,?,?,?,?)";

        try {

            pst = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, AtxtNome.getText());
            pst.setString(2, txtCPFuser.getText().replaceAll("[^0-9]", ""));
            pst.setString(3, txtTel.getText().replaceAll("[^0-9]", ""));
            pst.setString(4, AtxtEmail.getText());
            pst.setString(5, txtCep.getText());
            pst.setString(6, AtxtEnd.getText());
            pst.setString(7, AtxtCidade.getText());
            pst.setString(8, AtxtBairro.getText());
            pst.setString(9, cboUf.getSelectedItem().toString());
            pst.setString(10, AtxtObs.getText());

            // nome e telefone sao campos obrigatorios
            if ((AtxtNome.getText().trim().isEmpty())
                    || (txtTel.getText().trim().replaceAll("[^0-9]", "").isEmpty())) {

                JOptionPane.showInternalMessageDialog(null,
                        "Preencha todos os campos obrigatórios!\n"
                        + "Nome; \nTelefone; \n");

            } else {
                if (emailEncontrado) {
                    if (cpfEncontrado) {
                        if (telEncontrado) {
                            if (cepEncontrado) {
                                System.out.println("cepEncontrado!");
                                // se vdd, entao cep encontrado
                                int adicionado = pst.executeUpdate();

                                if (adicionado > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            "Cadastrado com sucesso!");
                                    rs = pst.getGeneratedKeys();
                                    if (rs.next()) {
                                        idCli = rs.getInt(1); //idCli gerado
                                        getCadastrarLogCli(3); // insert
                                        limpar();
                                    }

                                }
                            } else {
                                // caso nao, cep nao encontrado
                                int resposta = JOptionPane.showConfirmDialog(null,
                                        "CEP não encontrado!"
                                        + " Deseja continuar com o cadastro?",
                                        "Confirmação",
                                        JOptionPane.YES_NO_OPTION);

                                if (resposta == JOptionPane.YES_OPTION) {
                                    cepEncontrado = true;
                                    cadastrar();
                                }

                            }

                        } else {
                            // caso nao, tel nao encontrado
                            int resposta = JOptionPane.showConfirmDialog(null,
                                    "Telefone não encontrado!"
                                    + " Deseja continuar com o cadastro?",
                                    "Confirmação",
                                    JOptionPane.YES_NO_OPTION);

                            if (resposta == JOptionPane.YES_OPTION) {
                                telEncontrado = true;
                                cadastrar();
                            }
                        }
                    } else {
                        // caso nao, cpf nao encontrado
                        int resposta = JOptionPane.showConfirmDialog(null,
                                "CPF não encontrado!"
                                + " Deseja continuar com o cadastro?",
                                "Confirmação",
                                JOptionPane.YES_NO_OPTION);

                        if (resposta == JOptionPane.YES_OPTION) {
                            cpfEncontrado = true;
                            cadastrar();
                        }
                    }
                } else { // email
                    int resposta = JOptionPane.showConfirmDialog(null,
                            "email nao encontrado! Deseja continuar com o cadastro?", "Confirmação", JOptionPane.YES_NO_OPTION);

                    if (resposta == JOptionPane.YES_OPTION) {
                        emailEncontrado = true;
                        cadastrar();
                    }
                }
            }
            }catch (Exception e) {
            System.out.println("erro cad func: \n" + e);
            JOptionPane.showMessageDialog(null, "Erro no sistema!\n"
                    + "entrar com um Administrador.");
        }

        }

    

    private void limpar() {
        AtxtNome.setText(null);
        txtTel.setText(null);
        cboUf.setSelectedItem("");
        AtxtEnd.setText(null);
        AtxtBairro.setText(null);
        AtxtCidade.setText(null);
        AtxtEmail.setText(null);
        txtCPFuser.setText(null);
        txtCep.setText(null);
        AtxtObs.setText(null);

    }

    private void procedCep() {

        Verifica_Validacao funcoes = new Verifica_Validacao();
        boolean verificacao = funcoes.validaCep(txtCep.getText());

        if (verificacao) {
            // se verificacao == true
            lblCep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_check_mark_accept_icon.png")));
            cepEncontrado = true;
        } else {
            // se verificacao == false
            lblCep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png")));
            cepEncontrado = false;
        }
    }

    private void procedCPF() {
        Verifica_Validacao funcoes = new Verifica_Validacao();
        boolean verificacao = funcoes.validaCPF(txtCPFuser.getText());

        if (verificacao) {
            // se verificacao == true
            lblCpf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_check_mark_accept_icon.png")));
            //System.out.println("pesquisou la!");
            cpfEncontrado = true;
        } else {
            // se verificacao == false
            lblCpf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png")));
            cpfEncontrado = false;

        }

        if (txtCPFuser.getText().contains(" ")) {
            lblCpf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png")));

        }

    }

    private void procedTel() {
        Verifica_Validacao funcoes = new Verifica_Validacao();
        boolean verificacao = funcoes.validaTel(txtTel.getText());

        if (verificacao) {
            // se verificacao == true
            lblTel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_check_mark_accept_icon.png")));
            //System.out.println("pesquisou la!");
            telEncontrado = true;
        } else {
            // se verificacao == false
            lblTel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png")));
            telEncontrado = false;
        }

        if (txtTel.getText().contains(" ")) {
            lblTel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png")));

        }
    }

    private void getCadastrarLogCli(int i) {
        // cadastra log 

        String nome = AtxtNome.getText();
        String tel = txtTel.getText();
        String mail = AtxtEmail.getText();
        String cpf = txtCPFuser.getText();
        String end = AtxtEnd.getText();
        String bairro = AtxtBairro.getText();
        String cidade = AtxtCidade.getText();
        String cep = txtCep.getText();
        String uf = cboUf.getSelectedItem().toString();
        String obs = AtxtObs.getText();

    }

    private void funcaoEmail() {
        Verifica_Validacao funcoes = new Verifica_Validacao();
        boolean verificacao = funcoes.verificarEmail(AtxtEmail.getText());
        if (verificacao) {
            // se verificacao == true
            lblEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_check_mark_accept_icon.png")));
            emailEncontrado = true;

        } else {
            // se verificacao == false
            lblEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png")));
            emailEncontrado = false;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnPrincipal = new javax.swing.JPanel();
        pnCima = new javax.swing.JPanel();
        lblPesquisa = new javax.swing.JLabel();
        txtPesquisar = new javax.swing.JTextField();
        btnAtt = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        pnMeio = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCli = new javax.swing.JTable();
        pnBaixo = new javax.swing.JPanel();
        lbl_Id1 = new javax.swing.JLabel();
        txt_Id = new javax.swing.JTextField();
        lblNome = new javax.swing.JLabel();
        lblCidade = new javax.swing.JLabel();
        lblBairro = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblUf = new javax.swing.JLabel();
        lblCpf = new javax.swing.JLabel();
        lblEnd = new javax.swing.JLabel();
        lblObs = new javax.swing.JLabel();
        btnUserCad = new javax.swing.JButton();
        txtCPFuser = new javax.swing.JFormattedTextField();
        cboUf = new javax.swing.JComboBox<>();
        jspObs = new javax.swing.JScrollPane();
        AtxtObs = new javax.swing.JTextArea();
        jspNome = new javax.swing.JScrollPane();
        AtxtNome = new javax.swing.JTextArea();
        jspEmail = new javax.swing.JScrollPane();
        AtxtEmail = new javax.swing.JTextArea();
        jspCidade = new javax.swing.JScrollPane();
        AtxtCidade = new javax.swing.JTextArea();
        jspBairro = new javax.swing.JScrollPane();
        AtxtBairro = new javax.swing.JTextArea();
        jspCep = new javax.swing.JScrollPane();
        AtxtEnd = new javax.swing.JTextArea();
        txtTel = new javax.swing.JFormattedTextField();
        lblTel = new javax.swing.JLabel();
        txtCep = new javax.swing.JFormattedTextField();
        lblCep = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(null);
        setClosable(true);
        setResizable(true);
        setTitle("Tela Cadastro Cliente");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnPrincipal.setBackground(new java.awt.Color(153, 153, 153));
        pnPrincipal.setMinimumSize(new java.awt.Dimension(800, 600));
        pnPrincipal.setPreferredSize(new java.awt.Dimension(800, 600));
        pnPrincipal.setLayout(new java.awt.GridBagLayout());

        pnCima.setBackground(new java.awt.Color(51, 51, 51));
        pnCima.setMinimumSize(new java.awt.Dimension(50, 50));
        pnCima.setPreferredSize(new java.awt.Dimension(50, 50));
        pnCima.setLayout(new java.awt.GridBagLayout());

        lblPesquisa.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblPesquisa.setForeground(new java.awt.Color(204, 204, 204));
        lblPesquisa.setText("Pesquisar:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(lblPesquisa, gridBagConstraints);

        txtPesquisar.setBackground(new java.awt.Color(204, 204, 204));
        txtPesquisar.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        txtPesquisar.setMinimumSize(new java.awt.Dimension(45, 45));
        txtPesquisar.setPreferredSize(new java.awt.Dimension(45, 45));
        txtPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesquisarKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(txtPesquisar, gridBagConstraints);

        btnAtt.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnAtt.setText("Atualizar");
        btnAtt.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnAtt.setMaximumSize(new java.awt.Dimension(45, 45));
        btnAtt.setMinimumSize(new java.awt.Dimension(45, 45));
        btnAtt.setPreferredSize(new java.awt.Dimension(45, 45));
        btnAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(btnAtt, gridBagConstraints);

        btnLimpar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnLimpar.setMaximumSize(new java.awt.Dimension(45, 45));
        btnLimpar.setMinimumSize(new java.awt.Dimension(45, 45));
        btnLimpar.setPreferredSize(new java.awt.Dimension(45, 45));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(btnLimpar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        pnPrincipal.add(pnCima, gridBagConstraints);

        pnMeio.setBackground(new java.awt.Color(102, 102, 102));
        pnMeio.setMinimumSize(new java.awt.Dimension(50, 50));
        pnMeio.setPreferredSize(new java.awt.Dimension(50, 50));
        pnMeio.setLayout(new java.awt.GridBagLayout());

        tblCli.setBackground(new java.awt.Color(102, 102, 102));
        tblCli.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblCli.setGridColor(new java.awt.Color(15, 15, 15));
        tblCli.setRequestFocusEnabled(false);
        tblCli.setSelectionBackground(new java.awt.Color(0, 51, 204));
        tblCli.setSurrendersFocusOnKeystroke(true);
        tblCli.getTableHeader().setReorderingAllowed(false);
        tblCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCliMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblCli);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnMeio.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        pnPrincipal.add(pnMeio, gridBagConstraints);

        pnBaixo.setBackground(new java.awt.Color(51, 51, 51));
        pnBaixo.setMinimumSize(new java.awt.Dimension(50, 50));
        pnBaixo.setPreferredSize(new java.awt.Dimension(50, 50));
        pnBaixo.setLayout(new java.awt.GridBagLayout());

        lbl_Id1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbl_Id1.setForeground(new java.awt.Color(204, 204, 204));
        lbl_Id1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_Id1.setText("ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lbl_Id1, gridBagConstraints);

        txt_Id.setBackground(new java.awt.Color(204, 204, 204));
        txt_Id.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        txt_Id.setEnabled(false);
        txt_Id.setMinimumSize(new java.awt.Dimension(45, 45));
        txt_Id.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txt_Id, gridBagConstraints);

        lblNome.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblNome.setForeground(new java.awt.Color(204, 204, 204));
        lblNome.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblNome.setText("Nome:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblNome, gridBagConstraints);

        lblCidade.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCidade.setForeground(new java.awt.Color(204, 204, 204));
        lblCidade.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCidade.setText("Cidade:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblCidade, gridBagConstraints);

        lblBairro.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblBairro.setForeground(new java.awt.Color(204, 204, 204));
        lblBairro.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblBairro.setText("Bairro:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblBairro, gridBagConstraints);

        lblEmail.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblEmail.setForeground(new java.awt.Color(204, 204, 204));
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png"))); // NOI18N
        lblEmail.setText("Email:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblEmail, gridBagConstraints);

        lblUf.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblUf.setForeground(new java.awt.Color(204, 204, 204));
        lblUf.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblUf.setText("UF:");
        lblUf.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblUf, gridBagConstraints);

        lblCpf.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCpf.setForeground(new java.awt.Color(204, 204, 204));
        lblCpf.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCpf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png"))); // NOI18N
        lblCpf.setText("CPF:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblCpf, gridBagConstraints);

        lblEnd.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblEnd.setForeground(new java.awt.Color(204, 204, 204));
        lblEnd.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblEnd.setText("Endereço:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblEnd, gridBagConstraints);

        lblObs.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblObs.setForeground(new java.awt.Color(204, 204, 204));
        lblObs.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblObs.setText("Obs:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblObs, gridBagConstraints);

        btnUserCad.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnUserCad.setText("Cadastrar");
        btnUserCad.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnUserCad.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        btnUserCad.setMinimumSize(new java.awt.Dimension(45, 45));
        btnUserCad.setPreferredSize(new java.awt.Dimension(45, 45));
        btnUserCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserCadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(btnUserCad, gridBagConstraints);

        txtCPFuser.setBackground(new java.awt.Color(204, 204, 204));
        txtCPFuser.setBorder(null);
        try {
            txtCPFuser.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCPFuser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCPFuser.setText("");
        txtCPFuser.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        txtCPFuser.setMinimumSize(new java.awt.Dimension(45, 45));
        txtCPFuser.setPreferredSize(new java.awt.Dimension(45, 45));
        txtCPFuser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCPFuserKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCPFuserKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtCPFuser, gridBagConstraints);

        cboUf.setBackground(new java.awt.Color(204, 204, 204));
        cboUf.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        cboUf.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        cboUf.setBorder(null);
        cboUf.setMinimumSize(new java.awt.Dimension(45, 45));
        cboUf.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(cboUf, gridBagConstraints);

        jspObs.setBackground(new java.awt.Color(204, 204, 204));
        jspObs.setBorder(null);
        jspObs.setMinimumSize(new java.awt.Dimension(45, 45));
        jspObs.setPreferredSize(new java.awt.Dimension(45, 45));

        AtxtObs.setBackground(new java.awt.Color(204, 204, 204));
        AtxtObs.setColumns(5);
        AtxtObs.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        AtxtObs.setLineWrap(true);
        AtxtObs.setRows(1);
        AtxtObs.setToolTipText("");
        AtxtObs.setWrapStyleWord(true);
        AtxtObs.setBorder(null);
        AtxtObs.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtObs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AtxtObsKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AtxtObsKeyTyped(evt);
            }
        });
        jspObs.setViewportView(AtxtObs);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspObs, gridBagConstraints);

        jspNome.setBackground(new java.awt.Color(204, 204, 204));
        jspNome.setBorder(null);
        jspNome.setMinimumSize(new java.awt.Dimension(45, 45));
        jspNome.setPreferredSize(new java.awt.Dimension(45, 45));

        AtxtNome.setBackground(new java.awt.Color(204, 204, 204));
        AtxtNome.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        AtxtNome.setLineWrap(true);
        AtxtNome.setToolTipText("");
        AtxtNome.setWrapStyleWord(true);
        AtxtNome.setBorder(null);
        AtxtNome.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AtxtNomeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AtxtNomeKeyTyped(evt);
            }
        });
        jspNome.setViewportView(AtxtNome);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspNome, gridBagConstraints);

        jspEmail.setBackground(new java.awt.Color(204, 204, 204));
        jspEmail.setBorder(null);
        jspEmail.setMinimumSize(new java.awt.Dimension(45, 45));
        jspEmail.setPreferredSize(new java.awt.Dimension(45, 45));

        AtxtEmail.setBackground(new java.awt.Color(204, 204, 204));
        AtxtEmail.setColumns(5);
        AtxtEmail.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        AtxtEmail.setLineWrap(true);
        AtxtEmail.setRows(1);
        AtxtEmail.setToolTipText("");
        AtxtEmail.setWrapStyleWord(true);
        AtxtEmail.setBorder(null);
        AtxtEmail.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AtxtEmailKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AtxtEmailKeyTyped(evt);
            }
        });
        jspEmail.setViewportView(AtxtEmail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspEmail, gridBagConstraints);

        jspCidade.setBackground(new java.awt.Color(204, 204, 204));
        jspCidade.setBorder(null);
        jspCidade.setMinimumSize(new java.awt.Dimension(45, 45));
        jspCidade.setPreferredSize(new java.awt.Dimension(45, 45));

        AtxtCidade.setBackground(new java.awt.Color(204, 204, 204));
        AtxtCidade.setColumns(5);
        AtxtCidade.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        AtxtCidade.setLineWrap(true);
        AtxtCidade.setRows(1);
        AtxtCidade.setToolTipText("");
        AtxtCidade.setWrapStyleWord(true);
        AtxtCidade.setBorder(null);
        AtxtCidade.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtCidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AtxtCidadeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AtxtCidadeKeyTyped(evt);
            }
        });
        jspCidade.setViewportView(AtxtCidade);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspCidade, gridBagConstraints);

        jspBairro.setBackground(new java.awt.Color(204, 204, 204));
        jspBairro.setBorder(null);
        jspBairro.setMinimumSize(new java.awt.Dimension(45, 45));
        jspBairro.setPreferredSize(new java.awt.Dimension(45, 45));

        AtxtBairro.setBackground(new java.awt.Color(204, 204, 204));
        AtxtBairro.setColumns(5);
        AtxtBairro.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        AtxtBairro.setLineWrap(true);
        AtxtBairro.setRows(1);
        AtxtBairro.setToolTipText("");
        AtxtBairro.setWrapStyleWord(true);
        AtxtBairro.setBorder(null);
        AtxtBairro.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtBairro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AtxtBairroKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AtxtBairroKeyTyped(evt);
            }
        });
        jspBairro.setViewportView(AtxtBairro);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspBairro, gridBagConstraints);

        jspCep.setBackground(new java.awt.Color(204, 204, 204));
        jspCep.setBorder(null);
        jspCep.setMinimumSize(new java.awt.Dimension(45, 45));
        jspCep.setPreferredSize(new java.awt.Dimension(45, 45));

        AtxtEnd.setBackground(new java.awt.Color(204, 204, 204));
        AtxtEnd.setColumns(5);
        AtxtEnd.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        AtxtEnd.setLineWrap(true);
        AtxtEnd.setRows(1);
        AtxtEnd.setToolTipText("");
        AtxtEnd.setWrapStyleWord(true);
        AtxtEnd.setBorder(null);
        AtxtEnd.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtEnd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AtxtEndKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AtxtEndKeyTyped(evt);
            }
        });
        jspCep.setViewportView(AtxtEnd);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspCep, gridBagConstraints);

        txtTel.setBackground(new java.awt.Color(204, 204, 204));
        txtTel.setBorder(null);
        try {
            txtTel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTel.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        txtTel.setMinimumSize(new java.awt.Dimension(45, 45));
        txtTel.setPreferredSize(new java.awt.Dimension(45, 45));
        txtTel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTelKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtTel, gridBagConstraints);

        lblTel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTel.setForeground(new java.awt.Color(204, 204, 204));
        lblTel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png"))); // NOI18N
        lblTel.setText("Telefone:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblTel, gridBagConstraints);

        txtCep.setBackground(new java.awt.Color(204, 204, 204));
        txtCep.setBorder(null);
        try {
            txtCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCep.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCep.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        txtCep.setMinimumSize(new java.awt.Dimension(45, 45));
        txtCep.setPreferredSize(new java.awt.Dimension(45, 45));
        txtCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCepActionPerformed(evt);
            }
        });
        txtCep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCepKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCepKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtCep, gridBagConstraints);

        lblCep.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCep.setForeground(new java.awt.Color(204, 204, 204));
        lblCep.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/circle_xmark_icon.png"))); // NOI18N
        lblCep.setText("CEP:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblCep, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnPrincipal.add(pnBaixo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnPrincipal, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttActionPerformed
        consultaCliente();
        procedAjustaTable();
    }//GEN-LAST:event_btnAttActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void tblCliMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCliMousePressed
        tblCli.editingCanceled(null);
        tblCli.editingStopped(null);
    }//GEN-LAST:event_tblCliMousePressed

    private void btnUserCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserCadActionPerformed
        cadastrar();
    }//GEN-LAST:event_btnUserCadActionPerformed

    private void txtCPFuserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCPFuserKeyReleased
        String numeros = "0987654321";
        //O método contains é uma função em Java que é usada
        //para verificar se uma determinada sequência de
        //caracteres (uma substring) está contida em numeros.
        if (!numeros.contains(evt.getKeyChar() + "")) {
            //evt.consume() é um método utilizado para consumir
            //eventos, ou seja, impedir que um evento seja processado
            evt.consume();

        }
        procedCPF();


    }//GEN-LAST:event_txtCPFuserKeyReleased

    private void txtCPFuserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCPFuserKeyTyped
        String numeros = "0987654321";
        //O método contains é uma função em Java que é usada
        //para verificar se uma determinada sequência de
        //caracteres (uma substring) está contida em numeros.
        if (!numeros.contains(evt.getKeyChar() + "")) {
            //evt.consume() é um método utilizado para consumir
            //eventos, ou seja, impedir que um evento seja processado
            evt.consume();

        }
        procedCPF();

    }//GEN-LAST:event_txtCPFuserKeyTyped

    private void AtxtObsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtObsKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtObs.setEditable(true);
        }

        AtxtObs.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        AtxtObs.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

    }//GEN-LAST:event_AtxtObsKeyReleased

    private void AtxtObsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtObsKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtObs.setEditable(true);
        }

        AtxtObs.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        AtxtObs.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

    }//GEN-LAST:event_AtxtObsKeyTyped

    private void AtxtNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtNomeKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtNome.setEditable(true);
        }

        AtxtNome.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        AtxtNome.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

    }//GEN-LAST:event_AtxtNomeKeyReleased

    private void AtxtNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtNomeKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtNome.setEditable(true);
        }

        // codigo que faz o JTextArea pular para o proximo campo
        AtxtNome.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        AtxtNome.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

    }//GEN-LAST:event_AtxtNomeKeyTyped

    private void AtxtEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtEmailKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        funcaoEmail();
    }//GEN-LAST:event_AtxtEmailKeyReleased

    private void AtxtEmailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtEmailKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        funcaoEmail();
    }//GEN-LAST:event_AtxtEmailKeyTyped

    private void AtxtCidadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtCidadeKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtCidade.setEditable(true);
        }

    }//GEN-LAST:event_AtxtCidadeKeyReleased

    private void AtxtCidadeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtCidadeKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtCidade.setEditable(true);
        }

    }//GEN-LAST:event_AtxtCidadeKeyTyped

    private void AtxtBairroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtBairroKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtBairro.setEditable(true);
        }

        AtxtBairro.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        AtxtBairro.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

    }//GEN-LAST:event_AtxtBairroKeyReleased

    private void AtxtBairroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtBairroKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtBairro.setEditable(true);
        }

        AtxtBairro.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        AtxtBairro.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

    }//GEN-LAST:event_AtxtBairroKeyTyped

    private void AtxtEndKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtEndKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtEnd.setEditable(true);
        }

        AtxtEnd.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        AtxtEnd.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

    }//GEN-LAST:event_AtxtEndKeyReleased

    private void AtxtEndKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtEndKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            AtxtEnd.setEditable(true);
        }

        AtxtEnd.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        AtxtEnd.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);

    }//GEN-LAST:event_AtxtEndKeyTyped

    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultaCliente();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    private void txtPesquisarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultaCliente();
    }//GEN-LAST:event_txtPesquisarKeyTyped

    private void txtTelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyReleased
        String numeros = "0987654321";
        if (!numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();

        }
        procedTel();
    }//GEN-LAST:event_txtTelKeyReleased

    private void txtTelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyTyped
        String numeros = "0987654321";
        if (!numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();

        }
        procedTel();
    }//GEN-LAST:event_txtTelKeyTyped

    private void txtCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCepActionPerformed

    private void txtCepKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCepKeyReleased
        String numeros = "0987654321";
        if (!numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        procedCep();
    }//GEN-LAST:event_txtCepKeyReleased

    private void txtCepKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCepKeyTyped
        String numeros = "0987654321";
        if (!numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        procedCep();
    }//GEN-LAST:event_txtCepKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AtxtBairro;
    private javax.swing.JTextArea AtxtCidade;
    private javax.swing.JTextArea AtxtEmail;
    private javax.swing.JTextArea AtxtEnd;
    private javax.swing.JTextArea AtxtNome;
    private javax.swing.JTextArea AtxtObs;
    private javax.swing.JButton btnAtt;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnUserCad;
    private javax.swing.JComboBox<String> cboUf;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jspBairro;
    private javax.swing.JScrollPane jspCep;
    private javax.swing.JScrollPane jspCidade;
    private javax.swing.JScrollPane jspEmail;
    private javax.swing.JScrollPane jspNome;
    private javax.swing.JScrollPane jspObs;
    private javax.swing.JLabel lblBairro;
    private javax.swing.JLabel lblCep;
    private javax.swing.JLabel lblCidade;
    private javax.swing.JLabel lblCpf;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEnd;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblObs;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblTel;
    private javax.swing.JLabel lblUf;
    private javax.swing.JLabel lbl_Id1;
    private javax.swing.JPanel pnBaixo;
    private javax.swing.JPanel pnCima;
    private javax.swing.JPanel pnMeio;
    private javax.swing.JPanel pnPrincipal;
    private javax.swing.JTable tblCli;
    private javax.swing.JFormattedTextField txtCPFuser;
    private javax.swing.JFormattedTextField txtCep;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JFormattedTextField txtTel;
    private javax.swing.JTextField txt_Id;
    // End of variables declaration//GEN-END:variables
}
