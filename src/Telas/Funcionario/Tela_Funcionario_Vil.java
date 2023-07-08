
package Telas.Funcionario;

import Funcoes.Verifica_Validacao;
import java.sql.*;
// chama o arquivo de conexao com o banco de dados
import Connection.ConnectionClass;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author DIOGO e eric
 */

public class Tela_Funcionario_Vil extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    boolean isit = false;
    public Tela_Funcionario_Vil() {
        initComponents();
        conexao = ConnectionClass.conector();
        consultaFuncionario();
        procedAjustaTable();

    }

    private void consultaFuncionario() {
        //OBS: TIRAR ESSE COMANDO SQL DA TELA E POR EM UMA CLASSE SEPARADA POR SEGURANCA

        String sql = "select idFunc as ID, nomeFunc as Nome, Telefone, Email,"
                + " login, RG, CPF, DataDeNasc as 'Data de Nascimento', Sexo, Cargo,"
                + " Setor, Endereco as Endereço, Bairro, Cidade, CEP, UF, Obs"
                + " from tb_funcionario where nomeFunc like ?"
                + " AND situacao <> 'Sistema'";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisar.getText() + "%");
            rs = pst.executeQuery();
            //procedAjustaTable();
            tblFunc.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "consultaFuncionario Erro: \n" + e);
        }
    }

    private void setarCampos() {

        int setar = tblFunc.getSelectedRow();
        txt_Id.setText(tblFunc.getModel().getValueAt(setar, 0).toString());
        AtxtNome.setText(tblFunc.getModel().getValueAt(setar, 1).toString());
        txtTel.setText(tblFunc.getModel().getValueAt(setar, 2).toString());
        AtxtEmail.setText(tblFunc.getModel().getValueAt(setar, 3).toString());
        txtLogin.setText(tblFunc.getModel().getValueAt(setar, 4).toString());
        txtRGuser.setText(tblFunc.getModel().getValueAt(setar, 5).toString());
        txtCPFuser.setText(tblFunc.getModel().getValueAt(setar, 6).toString());
        // funcao que converte - para /
        txtDataUser.setText(funcaoDataNasc(tblFunc.getModel().getValueAt(setar, 7).toString()));
        // funcao que verifica
        cboSexoUser.setText(funcaoSexo(tblFunc.getModel().getValueAt(setar, 8).toString()));
        cboUserCargo.setText(tblFunc.getModel().getValueAt(setar, 9).toString());
        cboSetorUser.setText(tblFunc.getModel().getValueAt(setar, 10).toString());
        AtxtEnd.setText(tblFunc.getModel().getValueAt(setar, 11).toString());
        AtxtBairro.setText(tblFunc.getModel().getValueAt(setar, 12).toString());
        AtxtCidade.setText(tblFunc.getModel().getValueAt(setar, 13).toString());
        txtCep.setText(tblFunc.getModel().getValueAt(setar, 14).toString());
        cboUFuser.setText(tblFunc.getModel().getValueAt(setar, 15).toString());
        AtxtObs.setText(tblFunc.getModel().getValueAt(setar, 16).toString());

    }

    private void limpar() {
        txt_Id.setText("...");
        AtxtNome.setText("...");
        txtTel.setText("...");
        txtLogin.setText("...");
        cboUserCargo.setText("...");
        cboSexoUser.setText("...");
        cboSetorUser.setText("...");
        cboUFuser.setText("...");
        AtxtEnd.setText("...");
        AtxtBairro.setText("...");
        AtxtCidade.setText("...");
        AtxtEmail.setText("...");
        txtRGuser.setText("...");
        txtDataUser.setText("...");
        txtCPFuser.setText("...");
        txtCep.setText("...");
        AtxtObs.setText("...");

    }

    private void procedAjustaTable() {

        //tblFunc.setGridColor(Color.RGBtoHSB(ERROR, WIDTH, ABORT, "0F0F0F"));
        tblFunc.setShowGrid(true);
        tblFunc.setRowHeight(30);

        //Define o modo de redimensionamento automático da tabela como 
        //"AUTO_RESIZE_OFF", que desabilita o redimensionamento automático da
        //largura das colunas.
        tblFunc.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < tblFunc.getColumnCount(); column++) {
            TableColumn ColunaTable = tblFunc.getColumnModel().getColumn(column);
            // largura preferida da coluna(mínima)
            int miniWidth = ColunaTable.getMinWidth();
            // largura máxima da coluna.
            int maxWidth = ColunaTable.getMaxWidth();

            // Percorre cada linha da tabela para obter o conteúdo da célula.
            for (int linhas = 0; linhas < tblFunc.getRowCount(); linhas++) {
                // Obtém o renderizador(o valor ) de célula para a célula especificada.
                TableCellRenderer renderizadorCelula = tblFunc.getCellRenderer(linhas, column);
                // Prepara o renderizador para desenhar na célula especificada.
                Component c = tblFunc.prepareRenderer(renderizadorCelula, linhas, column);
                // Obtém a largura preferida do componente que representa a célula.
                int width = c.getPreferredSize().width + tblFunc.getIntercellSpacing().width;
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
            ColunaTable.setPreferredWidth(miniWidth + (5 * tblFunc.getColumnName(column).length()));
            //ColunaTable.setPreferredWidth(preferredWidth + ColunaTable.getWidth());
        }

    }

    private String funcaoSexo(String a) {
        Verifica_Validacao funcoes = new Verifica_Validacao();
        String verificacao = funcoes.verificarSexo(a);
        return verificacao;
    }

    private String funcaoDataNasc(String a) {
        Verifica_Validacao funcoes = new Verifica_Validacao();
        String verificacao = funcoes.verificaDataNasc(a);
        return verificacao;
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
        btnLimpar = new javax.swing.JButton();
        pnMeio = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFunc = new javax.swing.JTable();
        pnBaixo = new javax.swing.JPanel();
        lbl_Id1 = new javax.swing.JLabel();
        lblCep = new javax.swing.JLabel();
        lblCargo = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        lblCidade = new javax.swing.JLabel();
        lblSetor = new javax.swing.JLabel();
        lblLogin = new javax.swing.JLabel();
        lblBairro = new javax.swing.JLabel();
        lblSexo = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblUf = new javax.swing.JLabel();
        lblTel = new javax.swing.JLabel();
        lblCpf = new javax.swing.JLabel();
        lblEnd = new javax.swing.JLabel();
        lblRg = new javax.swing.JLabel();
        lblObs = new javax.swing.JLabel();
        lblDataNasc = new javax.swing.JLabel();
        txt_Id = new javax.swing.JLabel();
        AtxtNome = new javax.swing.JLabel();
        txtLogin = new javax.swing.JLabel();
        AtxtEmail = new javax.swing.JLabel();
        txtCPFuser = new javax.swing.JLabel();
        txtRGuser = new javax.swing.JLabel();
        txtDataUser = new javax.swing.JLabel();
        txtCep = new javax.swing.JLabel();
        AtxtCidade = new javax.swing.JLabel();
        AtxtBairro = new javax.swing.JLabel();
        cboUFuser = new javax.swing.JLabel();
        AtxtEnd = new javax.swing.JLabel();
        AtxtObs = new javax.swing.JLabel();
        cboUserCargo = new javax.swing.JLabel();
        cboSetorUser = new javax.swing.JLabel();
        cboSexoUser = new javax.swing.JLabel();
        txtTel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(null);
        setClosable(true);
        setResizable(true);
        setTitle("Tela Visualizar Funcionario");
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

        tblFunc.setBackground(new java.awt.Color(102, 102, 102));
        tblFunc.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblFunc.setModel(new javax.swing.table.DefaultTableModel(
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
        tblFunc.setGridColor(new java.awt.Color(15, 15, 15));
        tblFunc.setRequestFocusEnabled(false);
        tblFunc.setSelectionBackground(new java.awt.Color(0, 51, 204));
        tblFunc.setSurrendersFocusOnKeystroke(true);
        tblFunc.getTableHeader().setReorderingAllowed(false);
        tblFunc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFuncMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblFuncMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblFunc);

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
        pnBaixo.setForeground(new java.awt.Color(0, 0, 0));
        pnBaixo.setMinimumSize(new java.awt.Dimension(50, 50));
        pnBaixo.setPreferredSize(new java.awt.Dimension(50, 50));
        pnBaixo.setLayout(new java.awt.GridBagLayout());

        lbl_Id1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbl_Id1.setForeground(new java.awt.Color(204, 204, 204));
        lbl_Id1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_Id1.setText("ID:");
        lbl_Id1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lbl_Id1, gridBagConstraints);

        lblCep.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCep.setForeground(new java.awt.Color(204, 204, 204));
        lblCep.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCep.setText("CEP:");
        lblCep.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblCep, gridBagConstraints);

        lblCargo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCargo.setForeground(new java.awt.Color(204, 204, 204));
        lblCargo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCargo.setText("Cargo:");
        lblCargo.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblCargo, gridBagConstraints);

        lblNome.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblNome.setForeground(new java.awt.Color(204, 204, 204));
        lblNome.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblNome.setText("Nome:");
        lblNome.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
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
        lblCidade.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblCidade, gridBagConstraints);

        lblSetor.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblSetor.setForeground(new java.awt.Color(204, 204, 204));
        lblSetor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblSetor.setText("Setor:");
        lblSetor.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblSetor, gridBagConstraints);

        lblLogin.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblLogin.setForeground(new java.awt.Color(204, 204, 204));
        lblLogin.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblLogin.setText("Login:");
        lblLogin.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblLogin, gridBagConstraints);

        lblBairro.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblBairro.setForeground(new java.awt.Color(204, 204, 204));
        lblBairro.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblBairro.setText("Bairro:");
        lblBairro.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblBairro, gridBagConstraints);

        lblSexo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblSexo.setForeground(new java.awt.Color(204, 204, 204));
        lblSexo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblSexo.setText("Sexo:");
        lblSexo.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblSexo, gridBagConstraints);

        lblEmail.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblEmail.setForeground(new java.awt.Color(204, 204, 204));
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblEmail.setText("Email:");
        lblEmail.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
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
        lblUf.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblUf, gridBagConstraints);

        lblTel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTel.setForeground(new java.awt.Color(204, 204, 204));
        lblTel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTel.setText("Telefone:");
        lblTel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblTel, gridBagConstraints);

        lblCpf.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCpf.setForeground(new java.awt.Color(204, 204, 204));
        lblCpf.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCpf.setText("CPF:");
        lblCpf.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
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
        lblEnd.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblEnd, gridBagConstraints);

        lblRg.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblRg.setForeground(new java.awt.Color(204, 204, 204));
        lblRg.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblRg.setText("RG:");
        lblRg.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblRg, gridBagConstraints);

        lblObs.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblObs.setForeground(new java.awt.Color(204, 204, 204));
        lblObs.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblObs.setText("Obs:");
        lblObs.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblObs, gridBagConstraints);

        lblDataNasc.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblDataNasc.setForeground(new java.awt.Color(204, 204, 204));
        lblDataNasc.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDataNasc.setText("<html>Data de  <br> Nascimento </html>");
        lblDataNasc.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblDataNasc, gridBagConstraints);

        txt_Id.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txt_Id.setForeground(new java.awt.Color(204, 204, 204));
        txt_Id.setText("...");
        txt_Id.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        txt_Id.setMinimumSize(new java.awt.Dimension(45, 45));
        txt_Id.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txt_Id, gridBagConstraints);

        AtxtNome.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        AtxtNome.setForeground(new java.awt.Color(204, 204, 204));
        AtxtNome.setText("...");
        AtxtNome.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        AtxtNome.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtNome.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(AtxtNome, gridBagConstraints);

        txtLogin.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtLogin.setForeground(new java.awt.Color(204, 204, 204));
        txtLogin.setText("...");
        txtLogin.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        txtLogin.setMinimumSize(new java.awt.Dimension(45, 45));
        txtLogin.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtLogin, gridBagConstraints);

        AtxtEmail.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        AtxtEmail.setForeground(new java.awt.Color(204, 204, 204));
        AtxtEmail.setText("...");
        AtxtEmail.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        AtxtEmail.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtEmail.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(AtxtEmail, gridBagConstraints);

        txtCPFuser.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtCPFuser.setForeground(new java.awt.Color(204, 204, 204));
        txtCPFuser.setText("...");
        txtCPFuser.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        txtCPFuser.setMinimumSize(new java.awt.Dimension(45, 45));
        txtCPFuser.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtCPFuser, gridBagConstraints);

        txtRGuser.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtRGuser.setForeground(new java.awt.Color(204, 204, 204));
        txtRGuser.setText("...");
        txtRGuser.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        txtRGuser.setMinimumSize(new java.awt.Dimension(45, 45));
        txtRGuser.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtRGuser, gridBagConstraints);

        txtDataUser.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtDataUser.setForeground(new java.awt.Color(204, 204, 204));
        txtDataUser.setText("...");
        txtDataUser.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        txtDataUser.setMinimumSize(new java.awt.Dimension(45, 45));
        txtDataUser.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtDataUser, gridBagConstraints);

        txtCep.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtCep.setForeground(new java.awt.Color(204, 204, 204));
        txtCep.setText("...");
        txtCep.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        txtCep.setMinimumSize(new java.awt.Dimension(45, 45));
        txtCep.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtCep, gridBagConstraints);

        AtxtCidade.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        AtxtCidade.setForeground(new java.awt.Color(204, 204, 204));
        AtxtCidade.setText("...");
        AtxtCidade.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        AtxtCidade.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtCidade.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(AtxtCidade, gridBagConstraints);

        AtxtBairro.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        AtxtBairro.setForeground(new java.awt.Color(204, 204, 204));
        AtxtBairro.setText("...");
        AtxtBairro.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        AtxtBairro.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtBairro.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(AtxtBairro, gridBagConstraints);

        cboUFuser.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cboUFuser.setForeground(new java.awt.Color(204, 204, 204));
        cboUFuser.setText("...");
        cboUFuser.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboUFuser.setMinimumSize(new java.awt.Dimension(45, 45));
        cboUFuser.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(cboUFuser, gridBagConstraints);

        AtxtEnd.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        AtxtEnd.setForeground(new java.awt.Color(204, 204, 204));
        AtxtEnd.setText("...");
        AtxtEnd.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        AtxtEnd.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtEnd.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(AtxtEnd, gridBagConstraints);

        AtxtObs.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        AtxtObs.setForeground(new java.awt.Color(204, 204, 204));
        AtxtObs.setText("...");
        AtxtObs.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        AtxtObs.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtObs.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(AtxtObs, gridBagConstraints);

        cboUserCargo.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cboUserCargo.setForeground(new java.awt.Color(204, 204, 204));
        cboUserCargo.setText("...");
        cboUserCargo.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboUserCargo.setMinimumSize(new java.awt.Dimension(45, 45));
        cboUserCargo.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(cboUserCargo, gridBagConstraints);

        cboSetorUser.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cboSetorUser.setForeground(new java.awt.Color(204, 204, 204));
        cboSetorUser.setText("...");
        cboSetorUser.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboSetorUser.setMinimumSize(new java.awt.Dimension(45, 45));
        cboSetorUser.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(cboSetorUser, gridBagConstraints);

        cboSexoUser.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cboSexoUser.setForeground(new java.awt.Color(204, 204, 204));
        cboSexoUser.setText("...");
        cboSexoUser.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboSexoUser.setMinimumSize(new java.awt.Dimension(45, 45));
        cboSexoUser.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(cboSexoUser, gridBagConstraints);

        txtTel.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtTel.setForeground(new java.awt.Color(204, 204, 204));
        txtTel.setText("...");
        txtTel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        txtTel.setMinimumSize(new java.awt.Dimension(45, 45));
        txtTel.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtTel, gridBagConstraints);

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

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void tblFuncMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFuncMousePressed
        tblFunc.editingCanceled(null);
        tblFunc.editingStopped(null);
    }//GEN-LAST:event_tblFuncMousePressed

    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultaFuncionario();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    private void txtPesquisarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultaFuncionario();
    }//GEN-LAST:event_txtPesquisarKeyTyped

    private void tblFuncMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFuncMouseClicked
        setarCampos();
    }//GEN-LAST:event_tblFuncMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AtxtBairro;
    private javax.swing.JLabel AtxtCidade;
    private javax.swing.JLabel AtxtEmail;
    private javax.swing.JLabel AtxtEnd;
    private javax.swing.JLabel AtxtNome;
    private javax.swing.JLabel AtxtObs;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JLabel cboSetorUser;
    private javax.swing.JLabel cboSexoUser;
    private javax.swing.JLabel cboUFuser;
    private javax.swing.JLabel cboUserCargo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBairro;
    private javax.swing.JLabel lblCargo;
    private javax.swing.JLabel lblCep;
    private javax.swing.JLabel lblCidade;
    private javax.swing.JLabel lblCpf;
    private javax.swing.JLabel lblDataNasc;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEnd;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblObs;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblRg;
    private javax.swing.JLabel lblSetor;
    private javax.swing.JLabel lblSexo;
    private javax.swing.JLabel lblTel;
    private javax.swing.JLabel lblUf;
    private javax.swing.JLabel lbl_Id1;
    private javax.swing.JPanel pnBaixo;
    private javax.swing.JPanel pnCima;
    private javax.swing.JPanel pnMeio;
    private javax.swing.JPanel pnPrincipal;
    private javax.swing.JTable tblFunc;
    private javax.swing.JLabel txtCPFuser;
    private javax.swing.JLabel txtCep;
    private javax.swing.JLabel txtDataUser;
    private javax.swing.JLabel txtLogin;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JLabel txtRGuser;
    private javax.swing.JLabel txtTel;
    private javax.swing.JLabel txt_Id;
    // End of variables declaration//GEN-END:variables
}
