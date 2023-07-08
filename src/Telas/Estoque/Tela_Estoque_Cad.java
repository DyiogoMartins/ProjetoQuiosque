package Telas.Estoque;

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

public class Tela_Estoque_Cad extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public Tela_Estoque_Cad() {
        initComponents();
        conexao = ConnectionClass.conector();
        consultarProduto();
        procedAjustaTable();

    }

    public void limpar() {
        AtxtProd.setText(null);
        //jspQnt.setValue(0);
        AtxtObs.setText(null);
        cboCategoria.setSelectedItem("");
        txt_IdProd.setText(null);
    }

    private void procedAjustaTable() {

        //tblFunc.setGridColor(Color.RGBtoHSB(ERROR, WIDTH, ABORT, "0F0F0F"));
        tblProd.setShowGrid(true);
        tblProd.setRowHeight(30);
        //Define o modo de redimensionamento automático da tabela como 
        //"AUTO_RESIZE_OFF", que desabilita o redimensionamento automático da
        //largura das colunas.
        tblProd.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < tblProd.getColumnCount(); column++) {
            TableColumn ColunaTable = tblProd.getColumnModel().getColumn(column);
            // largura preferida da coluna(mínima)
            int miniWidth = ColunaTable.getMinWidth();
            // largura máxima da coluna.
            int maxWidth = ColunaTable.getMaxWidth();

            // Percorre cada linha da tabela para obter o conteúdo da célula.
            for (int linhas = 0; linhas < tblProd.getRowCount(); linhas++) {
                // Obtém o renderizador(o valor ) de célula para a célula especificada.
                TableCellRenderer renderizadorCelula = tblProd.getCellRenderer(linhas, column);
                // Prepara o renderizador para desenhar na célula especificada.
                Component c = tblProd.prepareRenderer(renderizadorCelula, linhas, column);
                // Obtém a largura preferida do componente que representa a célula.
                int width = c.getPreferredSize().width + tblProd.getIntercellSpacing().width;
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
            ColunaTable.setPreferredWidth(miniWidth + (20 * tblProd.getColumnName(column).length()));
            //ColunaTable.setPreferredWidth(preferredWidth + ColunaTable.getWidth());
        }

    }

    private void consultarProduto() {

        String sql = "SELECT tb_estoque.idEstoq as 'ID Produto',"
                + " tb_estoque.Produto,"
                + " tb_estoque.categoria as 'Categoria',"
                + " tb_estoque.Qnt as 'Quantidade',"
                + " tb_estoque.obs as 'Observação',"
                + " DATE_FORMAT(Data_Hora, '%d/%m/%Y %H:%i:%s') AS 'Data Hora'"
                + " FROM tb_estoque"
                + " WHERE tb_estoque.Produto like ?";

        //tb_estoque.Qnt as 'Quantidade',
        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesProd.getText() + "%");
            rs = pst.executeQuery();

            tblProd.setModel(DbUtils.resultSetToTableModel(rs));
            //tblPro.getTableHeader().setReorderingAllowed(false);
            procedAjustaTable();

        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void cadastrar() {

        String sql = "insert into tb_estoque( Produto,"
                + " categoria, Qnt, obs) values(?, ?, ?, ?)";

        try {

            pst = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, AtxtProd.getText());
            pst.setString(2, cboCategoria.getSelectedItem().toString());
            pst.setInt(3, 0);
            pst.setString(4, AtxtObs.getText());

            if (AtxtObs.getText().isEmpty()
                    || (AtxtProd.getText().trim().isEmpty())
                    || (cboCategoria.getSelectedItem().toString().trim().isEmpty())) {

                JOptionPane.showInternalMessageDialog(null,
                        "Preencha todos os campos obrigatórios!\n"
                        + "Produto; \nObs; \nCategoria;");

            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Cadastrado com sucesso!");

                    rs = pst.getGeneratedKeys();
                    if (rs.next()) {
                        int idEst = rs.getInt(1); //idCli gerado
                        getCadastrarLogEst(idEst, 3); // insert
                        limpar();
                    }

                }
            }

        } catch (Exception e) {

            if (e.toString().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null,
                        "Registro já existe no sistema!");
            }
        }

    }

    private void getCadastrarLogEst(int idEst, int i) {
        // cadastra log 
        System.out.println("entrei no getCadastrarLogEst");
        String nome = AtxtProd.getText();
        String categoria = cboCategoria.getSelectedItem().toString();
        String obs = AtxtObs.getText();

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
        txtPesProd = new javax.swing.JTextField();
        btnAttProd = new javax.swing.JButton();
        btnLimpProd = new javax.swing.JButton();
        pnMeio = new javax.swing.JPanel();
        jspProd = new javax.swing.JScrollPane();
        tblProd = new javax.swing.JTable();
        pnBaixo = new javax.swing.JPanel();
        lbl_Id = new javax.swing.JLabel();
        txt_IdProd = new javax.swing.JTextField();
        lblProduto = new javax.swing.JLabel();
        jspProduto = new javax.swing.JScrollPane();
        AtxtProd = new javax.swing.JTextArea();
        lblCateg = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox<>();
        lblObs = new javax.swing.JLabel();
        jspObs = new javax.swing.JScrollPane();
        AtxtObs = new javax.swing.JTextArea();
        btnrCad = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(null);
        setClosable(true);
        setResizable(true);
        setTitle("Tela Cadastro Estoque");
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
        lblPesquisa.setText("Pesquisar Produto:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(lblPesquisa, gridBagConstraints);

        txtPesProd.setBackground(new java.awt.Color(204, 204, 204));
        txtPesProd.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        txtPesProd.setMinimumSize(new java.awt.Dimension(45, 45));
        txtPesProd.setPreferredSize(new java.awt.Dimension(45, 45));
        txtPesProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesProdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPesProdKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(txtPesProd, gridBagConstraints);

        btnAttProd.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnAttProd.setText("Atualizar");
        btnAttProd.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnAttProd.setMaximumSize(new java.awt.Dimension(45, 45));
        btnAttProd.setMinimumSize(new java.awt.Dimension(45, 45));
        btnAttProd.setPreferredSize(new java.awt.Dimension(45, 45));
        btnAttProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttProdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(btnAttProd, gridBagConstraints);

        btnLimpProd.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnLimpProd.setText("Limpar");
        btnLimpProd.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnLimpProd.setMaximumSize(new java.awt.Dimension(45, 45));
        btnLimpProd.setMinimumSize(new java.awt.Dimension(45, 45));
        btnLimpProd.setPreferredSize(new java.awt.Dimension(45, 45));
        btnLimpProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpProdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnCima.add(btnLimpProd, gridBagConstraints);

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

        jspProd.setBorder(null);
        jspProd.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jspProd.setMinimumSize(new java.awt.Dimension(450, 400));

        tblProd.setBackground(new java.awt.Color(102, 102, 102));
        tblProd.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblProd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblProd.setGridColor(new java.awt.Color(15, 15, 15));
        tblProd.setRequestFocusEnabled(false);
        tblProd.setSelectionBackground(new java.awt.Color(0, 51, 204));
        tblProd.setSurrendersFocusOnKeystroke(true);
        tblProd.getTableHeader().setReorderingAllowed(false);
        tblProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblProdMousePressed(evt);
            }
        });
        jspProd.setViewportView(tblProd);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnMeio.add(jspProd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 150;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.3;
        pnPrincipal.add(pnMeio, gridBagConstraints);

        pnBaixo.setBackground(new java.awt.Color(51, 51, 51));
        pnBaixo.setMinimumSize(new java.awt.Dimension(50, 50));
        pnBaixo.setPreferredSize(new java.awt.Dimension(50, 50));
        pnBaixo.setLayout(new java.awt.GridBagLayout());

        lbl_Id.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbl_Id.setForeground(new java.awt.Color(204, 204, 204));
        lbl_Id.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_Id.setText("ID Produto:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lbl_Id, gridBagConstraints);

        txt_IdProd.setBackground(new java.awt.Color(204, 204, 204));
        txt_IdProd.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        txt_IdProd.setEnabled(false);
        txt_IdProd.setMinimumSize(new java.awt.Dimension(45, 45));
        txt_IdProd.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(txt_IdProd, gridBagConstraints);

        lblProduto.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblProduto.setForeground(new java.awt.Color(204, 204, 204));
        lblProduto.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblProduto.setText("Produto:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblProduto, gridBagConstraints);

        jspProduto.setBackground(new java.awt.Color(204, 204, 204));
        jspProduto.setBorder(null);
        jspProduto.setMinimumSize(new java.awt.Dimension(45, 45));
        jspProduto.setPreferredSize(new java.awt.Dimension(45, 45));

        AtxtProd.setBackground(new java.awt.Color(204, 204, 204));
        AtxtProd.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        AtxtProd.setLineWrap(true);
        AtxtProd.setToolTipText("");
        AtxtProd.setWrapStyleWord(true);
        AtxtProd.setBorder(null);
        AtxtProd.setMinimumSize(new java.awt.Dimension(45, 45));
        AtxtProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AtxtProdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                AtxtProdKeyTyped(evt);
            }
        });
        jspProduto.setViewportView(AtxtProd);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspProduto, gridBagConstraints);

        lblCateg.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCateg.setForeground(new java.awt.Color(219, 219, 219));
        lblCateg.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCateg.setText("Categoria:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblCateg, gridBagConstraints);

        cboCategoria.setBackground(new java.awt.Color(204, 204, 204));
        cboCategoria.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Carnes", "Produtos lácteos", "Vegetais", "Tubérculo", "Produtos de padaria", "Produtos congelados", "Grãos e cereais", "Bebidas", "Produtos enlatados e embalados", "Produtos de panificação", "Ingredientes especiais", "Temperos", "Não informado" }));
        cboCategoria.setBorder(null);
        cboCategoria.setMinimumSize(new java.awt.Dimension(45, 45));
        cboCategoria.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(cboCategoria, gridBagConstraints);

        lblObs.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblObs.setForeground(new java.awt.Color(204, 204, 204));
        lblObs.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblObs.setText("Obs:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblObs, gridBagConstraints);

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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspObs, gridBagConstraints);

        btnrCad.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnrCad.setText("Cadastrar");
        btnrCad.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnrCad.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        btnrCad.setMinimumSize(new java.awt.Dimension(45, 45));
        btnrCad.setPreferredSize(new java.awt.Dimension(45, 45));
        btnrCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrCadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(btnrCad, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
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

    private void btnAttProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttProdActionPerformed
        consultarProduto();
        procedAjustaTable();
    }//GEN-LAST:event_btnAttProdActionPerformed

    private void btnLimpProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpProdActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimpProdActionPerformed

    private void tblProdMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdMousePressed
        tblProd.editingCanceled(null);
        tblProd.editingStopped(null);
    }//GEN-LAST:event_tblProdMousePressed

    private void btnrCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrCadActionPerformed
        cadastrar();
    }//GEN-LAST:event_btnrCadActionPerformed

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

    private void txtPesProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesProdKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultarProduto();
    }//GEN-LAST:event_txtPesProdKeyReleased

    private void txtPesProdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesProdKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultarProduto();
    }//GEN-LAST:event_txtPesProdKeyTyped

    private void AtxtProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtProdKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_AtxtProdKeyReleased

    private void AtxtProdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtProdKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_AtxtProdKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AtxtObs;
    private javax.swing.JTextArea AtxtProd;
    private javax.swing.JButton btnAttProd;
    private javax.swing.JButton btnLimpProd;
    private javax.swing.JButton btnrCad;
    private javax.swing.JComboBox<String> cboCategoria;
    private javax.swing.JScrollPane jspObs;
    private javax.swing.JScrollPane jspProd;
    private javax.swing.JScrollPane jspProduto;
    private javax.swing.JLabel lblCateg;
    private javax.swing.JLabel lblObs;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblProduto;
    private javax.swing.JLabel lbl_Id;
    private javax.swing.JPanel pnBaixo;
    private javax.swing.JPanel pnCima;
    private javax.swing.JPanel pnMeio;
    private javax.swing.JPanel pnPrincipal;
    private javax.swing.JTable tblProd;
    private javax.swing.JTextField txtPesProd;
    private javax.swing.JTextField txt_IdProd;
    // End of variables declaration//GEN-END:variables
}
