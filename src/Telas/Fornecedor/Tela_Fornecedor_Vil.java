/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Telas.Fornecedor;

import Funcoes.Verifica_Validacao;
import java.sql.*;
// chama o arquivo de conexao com o banco de dados
import Connection.ConnectionClass;
import net.proteanit.sql.DbUtils;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author DIOGO e eric
 */
public class Tela_Fornecedor_Vil extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    boolean isit = false;

    /**
     * Creates new form teste
     */
    public Tela_Fornecedor_Vil() {
        initComponents();
        conexao = ConnectionClass.conector();
        consultarFornecedor();
        procedAjustaTable();

    }

    private void consultarFornecedor() {
        // pesquisar no banco de dados os fornecedores ja cadastrados
        String sql = "select idForn as ID, fornecedor, CNPJ, \n"
                + " CEP, endereco as endereço, cidade as Cidade, "
                + "bairro as Bairro, UF, email as Email, telefone as Telefone ,"
                + "obs from tb_fornecedor where Fornecedor like ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisar.getText() + "%");
            rs = pst.executeQuery();
            // seta na tabela do sistema as consultas
            tblForn.setModel(DbUtils.resultSetToTableModel(rs));
            procedAjustaTable();
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
        }

        //procedAjustaTable();
    }

    private void setarCampos() {

        int setar = tblForn.getSelectedRow();

        txt_Id.setText(tblForn.getModel().getValueAt(setar, 0).toString());
        AtxtNome.setText(tblForn.getModel().getValueAt(setar, 1).toString());
        txtCnpj.setText(tblForn.getModel().getValueAt(setar, 2).toString());
        txtCep.setText(tblForn.getModel().getValueAt(setar, 3).toString());
        AtxtEnd.setText(tblForn.getModel().getValueAt(setar, 4).toString());
        AtxtCidade.setText(tblForn.getModel().getValueAt(setar, 5).toString());
        AtxtBairro.setText(tblForn.getModel().getValueAt(setar, 6).toString());
        cboUF.setText(tblForn.getModel().getValueAt(setar, 7).toString());
        AtxtEmail.setText(tblForn.getModel().getValueAt(setar, 8).toString());
        txtTel.setText(tblForn.getModel().getValueAt(setar, 9).toString());
        AtxtObs.setText(tblForn.getModel().getValueAt(setar, 10).toString());
    }

    private void limpar() {
        txt_Id.setText("...");
        AtxtNome.setText("...");
        txtTel.setText("...");
        cboUF.setText("...");
        AtxtEnd.setText("...");
        AtxtBairro.setText("...");
        AtxtCidade.setText("...");
        AtxtEmail.setText("...");
        txtCnpj.setText("...");
        txtCep.setText("...");
        AtxtObs.setText("...");

    }

    private void procedAjustaTable() {

        //tblForn.setGridColor(Color.RGBtoHSB(ERROR, WIDTH, ABORT, "0F0F0F"));
        tblForn.setShowGrid(true);
        tblForn.setRowHeight(30);

        //Define o modo de redimensionamento automático da tabela como 
        //"AUTO_RESIZE_OFF", que desabilita o redimensionamento automático da
        //largura das colunas.
        tblForn.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < tblForn.getColumnCount(); column++) {
            TableColumn ColunaTable = tblForn.getColumnModel().getColumn(column);
            // largura preferida da coluna(mínima)
            int miniWidth = ColunaTable.getMinWidth();
            // largura máxima da coluna.
            int maxWidth = ColunaTable.getMaxWidth();

            // Percorre cada linha da tabela para obter o conteúdo da célula.
            for (int linhas = 0; linhas < tblForn.getRowCount(); linhas++) {
                // Obtém o renderizador(o valor ) de célula para a célula especificada.
                TableCellRenderer renderizadorCelula = tblForn.getCellRenderer(linhas, column);
                // Prepara o renderizador para desenhar na célula especificada.
                Component c = tblForn.prepareRenderer(renderizadorCelula, linhas, column);
                // Obtém a largura preferida do componente que representa a célula.
                int width = c.getPreferredSize().width + tblForn.getIntercellSpacing().width;
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
            ColunaTable.setPreferredWidth(miniWidth + (5 * tblForn.getColumnName(column).length()));
            //ColunaTable.setPreferredWidth(preferredWidth + ColunaTable.getWidth());
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
        btnLimpar = new javax.swing.JButton();
        pnMeio = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblForn = new javax.swing.JTable();
        pnBaixo = new javax.swing.JPanel();
        lbl_Id1 = new javax.swing.JLabel();
        lblCep = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        lblCidade = new javax.swing.JLabel();
        lblBairro = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblUf = new javax.swing.JLabel();
        lblTel = new javax.swing.JLabel();
        lblCnpj = new javax.swing.JLabel();
        lblEnd = new javax.swing.JLabel();
        lblObs = new javax.swing.JLabel();
        txt_Id = new javax.swing.JLabel();
        AtxtNome = new javax.swing.JLabel();
        AtxtEmail = new javax.swing.JLabel();
        txtCnpj = new javax.swing.JLabel();
        txtCep = new javax.swing.JLabel();
        AtxtCidade = new javax.swing.JLabel();
        AtxtBairro = new javax.swing.JLabel();
        cboUF = new javax.swing.JLabel();
        AtxtEnd = new javax.swing.JLabel();
        AtxtObs = new javax.swing.JLabel();
        txtTel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(null);
        setClosable(true);
        setResizable(true);
        setTitle("Tela Visualizar Fornecedor");
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

        tblForn.setBackground(new java.awt.Color(102, 102, 102));
        tblForn.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblForn.setModel(new javax.swing.table.DefaultTableModel(
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
        tblForn.setGridColor(new java.awt.Color(15, 15, 15));
        tblForn.setRequestFocusEnabled(false);
        tblForn.setSelectionBackground(new java.awt.Color(0, 51, 204));
        tblForn.setSurrendersFocusOnKeystroke(true);
        tblForn.getTableHeader().setReorderingAllowed(false);
        tblForn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFornMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblFornMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblForn);

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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblTel, gridBagConstraints);

        lblCnpj.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCnpj.setForeground(new java.awt.Color(204, 204, 204));
        lblCnpj.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCnpj.setText("CNPJ:");
        lblCnpj.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblCnpj, gridBagConstraints);

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

        lblObs.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblObs.setForeground(new java.awt.Color(204, 204, 204));
        lblObs.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblObs.setText("Obs:");
        lblObs.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblObs, gridBagConstraints);

        txt_Id.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
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

        AtxtNome.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
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

        AtxtEmail.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
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

        txtCnpj.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtCnpj.setForeground(new java.awt.Color(204, 204, 204));
        txtCnpj.setText("...");
        txtCnpj.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        txtCnpj.setMinimumSize(new java.awt.Dimension(45, 45));
        txtCnpj.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txtCnpj, gridBagConstraints);

        txtCep.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
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

        AtxtCidade.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
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

        AtxtBairro.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
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

        cboUF.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cboUF.setForeground(new java.awt.Color(204, 204, 204));
        cboUF.setText("...");
        cboUF.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboUF.setMinimumSize(new java.awt.Dimension(45, 45));
        cboUF.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(cboUF, gridBagConstraints);

        AtxtEnd.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
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

        AtxtObs.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        AtxtObs.setForeground(new java.awt.Color(204, 204, 204));
        AtxtObs.setText("...");
        AtxtObs.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        AtxtObs.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtObs.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(AtxtObs, gridBagConstraints);

        txtTel.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtTel.setForeground(new java.awt.Color(204, 204, 204));
        txtTel.setText("...");
        txtTel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        txtTel.setMinimumSize(new java.awt.Dimension(45, 45));
        txtTel.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
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

    private void tblFornMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFornMousePressed
        tblForn.editingCanceled(null);
        tblForn.editingStopped(null);
    }//GEN-LAST:event_tblFornMousePressed

    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultarFornecedor();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    private void txtPesquisarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultarFornecedor();
    }//GEN-LAST:event_txtPesquisarKeyTyped

    private void tblFornMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFornMouseClicked
        setarCampos();
    }//GEN-LAST:event_tblFornMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AtxtBairro;
    private javax.swing.JLabel AtxtCidade;
    private javax.swing.JLabel AtxtEmail;
    private javax.swing.JLabel AtxtEnd;
    private javax.swing.JLabel AtxtNome;
    private javax.swing.JLabel AtxtObs;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JLabel cboUF;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBairro;
    private javax.swing.JLabel lblCep;
    private javax.swing.JLabel lblCidade;
    private javax.swing.JLabel lblCnpj;
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
    private javax.swing.JTable tblForn;
    private javax.swing.JLabel txtCep;
    private javax.swing.JLabel txtCnpj;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JLabel txtTel;
    private javax.swing.JLabel txt_Id;
    // End of variables declaration//GEN-END:variables
}
