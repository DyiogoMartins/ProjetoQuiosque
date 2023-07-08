package Telas.Menu;

import java.sql.*;
// chama o arquivo de conexao com o banco de dados
import Connection.ConnectionClass;
import Funcoes.Funcoes_Sql;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.awt.Component;
import static java.awt.image.ImageObserver.HEIGHT;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author diogo e eric
 */
public class Tela_Menu_Alterar extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int counter;
    boolean isit = false;
    int setar;

    /**
     * Creates new form TelaVisualizarFunc
     */
    public Tela_Menu_Alterar() {
        initComponents();
        conexao = ConnectionClass.conector();
        consultarMenu();
        procedAjustaTable();
    }

    private void limpar() {
        txt_Id.setText(null);
        AtxtNome.setText(null);
        jspPreco.setValue(0.01);
        cboTipo.setSelectedItem(null);
        AtxtObs.setText(null);
    }

    private void setarCampos() {

        int setar = tblMenu.getSelectedRow();
        txt_Id.setText(tblMenu.getModel().getValueAt(setar, 0).toString());
        AtxtNome.setText(tblMenu.getModel().getValueAt(setar, 1).toString());
        cboTipo.setSelectedItem(tblMenu.getModel().getValueAt(setar, 2).toString());
        jspPreco.setValue(tblMenu.getModel().getValueAt(setar, 3));
        AtxtObs.setText(tblMenu.getModel().getValueAt(setar, 4).toString());
    }

    private void consultarMenu() {

        String sql = "  SELECT idMenu AS Menu, nomeMenu AS Nome,"
                + " Tipo, preco AS 'Preço', obs AS 'Observação',"
                + " Data_Hora AS 'Data Hora'"
                + " FROM tb_menu"
                + " WHERE nomeMenu LIKE ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisar.getText() + "%");
            rs = pst.executeQuery();

            tblMenu.setModel(DbUtils.resultSetToTableModel(rs));
            tblMenu.getTableHeader().setReorderingAllowed(false);
            procedAjustaTable();

        } catch (Exception e) {
            //   JOptionPane.showMessageDialog(null, e);
        }
    }

    private void procedAjustaTable() {

        //tblFunc.setGridColor(Color.RGBtoHSB(ERROR, WIDTH, ABORT, "0F0F0F"));
        tblMenu.setShowGrid(true);
        tblMenu.setRowHeight(30);

        //Define o modo de redimensionamento automático da tabela como 
        //"AUTO_RESIZE_OFF", que desabilita o redimensionamento automático da
        //largura das colunas.
        tblMenu.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int column = 0; column < tblMenu.getColumnCount(); column++) {
            TableColumn ColunaTable = tblMenu.getColumnModel().getColumn(column);
            // largura preferida da coluna(mínima)
            int miniWidth = ColunaTable.getMinWidth();
            // largura máxima da coluna.
            int maxWidth = ColunaTable.getMaxWidth();

            // Percorre cada linha da tabela para obter o conteúdo da célula.
            for (int linhas = 0; linhas < tblMenu.getRowCount(); linhas++) {
                // Obtém o renderizador(o valor ) de célula para a célula especificada.
                TableCellRenderer renderizadorCelula = tblMenu.getCellRenderer(linhas, column);
                // Prepara o renderizador para desenhar na célula especificada.
                Component c = tblMenu.prepareRenderer(renderizadorCelula, linhas, column);
                // Obtém a largura preferida do componente que representa a célula.
                int width = c.getPreferredSize().width + tblMenu.getIntercellSpacing().width;
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
            ColunaTable.setPreferredWidth(miniWidth + (30 * tblMenu.getColumnName(column).length()));
            //ColunaTable.setPreferredWidth(preferredWidth + ColunaTable.getWidth());
        }

    }

    private void alterar_menu() {
        String sql = "update tb_menu set"
                + " nomeMenu=?, Tipo=?, Preco=?, obs=?"
                + " where idMenu = ?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, AtxtNome.getText());
            pst.setString(2, cboTipo.getSelectedItem().toString());
            pst.setFloat(3, Float.parseFloat(jspPreco.getValue().toString().replace(",", ".")));
            pst.setString(4, AtxtObs.getText());
            pst.setString(5, txt_Id.getText());

            if ((AtxtNome.getText().trim().isEmpty())
                    || (cboTipo.getSelectedItem().toString().trim().isEmpty())
                    || (jspPreco.getValue().toString().trim().isEmpty())
                    || (txt_Id.getText().trim().isEmpty())) {

                JOptionPane.showInternalMessageDialog(null,
                        "Preencha todos os campos obrigatórios!\n"
                                + "Nome; \nTipo; \nPreco; \nObs; ");

            } else {

                int adicionado = pst.executeUpdate();
//                System.out.println(adicionado);

                if (adicionado > 0) {

                    JOptionPane.showInternalMessageDialog(null,
                            "Alteração com sucesso!");
                    getCadastrarLogMenu(1); //UPDATE
                    limpar();

                }

            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null,
                    "ERRO NO CATCH: "
                    + e);
        }
    }

    private void remover() {
        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este registro?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {

            String sql = "delete from tb_menu where idMenu = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txt_Id.getText());
                int apagado = pst.executeUpdate();

                if (apagado > 0) {

                    JOptionPane.showMessageDialog(null,
                            "Usuário removido com sucesso!");

                    getCadastrarLogMenu(2); //DELETE
                    limpar();
                    
                } else {
                    JOptionPane.showMessageDialog(null,
                            "ID(Identificação) do usúario nao escolhido!",
                            "Atenção!",
                            HEIGHT);
                }

            } catch (Exception e) {

//                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void getCadastrarLogMenu(int i) {
        // cadastra log menu
        int idMenu = Integer.parseInt(txt_Id.getText());
        String nome = AtxtNome.getText();
        Float preco = Float.parseFloat(jspPreco.getValue().toString().replace(",", "."));
        String tipo = cboTipo.getSelectedItem().toString();
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
        txtPesquisar = new javax.swing.JTextField();
        btnAtt = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        pnMeio = new javax.swing.JPanel();
        jspMenu = new javax.swing.JScrollPane();
        tblMenu = new javax.swing.JTable();
        pnBaixo = new javax.swing.JPanel();
        txt_Id = new javax.swing.JTextField();
        lbl_Id = new javax.swing.JLabel();
        lblMenu = new javax.swing.JLabel();
        jspNome = new javax.swing.JScrollPane();
        AtxtNome = new javax.swing.JTextArea();
        cboTipo = new javax.swing.JComboBox<>();
        lblTipo = new javax.swing.JLabel();
        lblObs = new javax.swing.JLabel();
        jspObs = new javax.swing.JScrollPane();
        AtxtObs = new javax.swing.JTextArea();
        jspPreco = new javax.swing.JSpinner();
        lblPreco = new javax.swing.JLabel();
        btnAlt = new javax.swing.JButton();
        btnExc = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(null);
        setClosable(true);
        setResizable(true);
        setTitle("Tela Alteração de Menu");
        setToolTipText("");
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

        jspMenu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        tblMenu.setBackground(new java.awt.Color(102, 102, 102));
        tblMenu.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblMenu.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMenu.setGridColor(new java.awt.Color(15, 15, 15));
        tblMenu.setRequestFocusEnabled(false);
        tblMenu.setSelectionBackground(new java.awt.Color(0, 51, 204));
        tblMenu.setSurrendersFocusOnKeystroke(true);
        tblMenu.getTableHeader().setReorderingAllowed(false);
        tblMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMenuMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMenuMousePressed(evt);
            }
        });
        jspMenu.setViewportView(tblMenu);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnMeio.add(jspMenu, gridBagConstraints);

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

        lbl_Id.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbl_Id.setForeground(new java.awt.Color(204, 204, 204));
        lbl_Id.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_Id.setText("ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lbl_Id, gridBagConstraints);

        lblMenu.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblMenu.setForeground(new java.awt.Color(204, 204, 204));
        lblMenu.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblMenu.setText("Nome:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblMenu, gridBagConstraints);

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

        cboTipo.setBackground(new java.awt.Color(204, 204, 204));
        cboTipo.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        cboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Bebidas", "Entradas ou Aperitivos", "Pratos de Brunch", "Pratos de Café da Manhã", "Pratos Especiais", "Pratos Gourmet", "Pratos Infantis", "Pratos Principais", "Pratos Veganos e Vegetarianos", "Pizza", "Saladas", "Sanduíches", "Sobremesas", "Sushi e Sashimi" }));
        cboTipo.setBorder(null);
        cboTipo.setMinimumSize(new java.awt.Dimension(45, 45));
        cboTipo.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(cboTipo, gridBagConstraints);

        lblTipo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTipo.setForeground(new java.awt.Color(204, 204, 204));
        lblTipo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblTipo.setText("Tipo:");
        lblTipo.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblTipo, gridBagConstraints);

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

        jspPreco.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jspPreco.setModel(new javax.swing.SpinnerNumberModel(0.01f, 0.0f, null, 0.01f));
        jspPreco.setBorder(null);
        jspPreco.setMinimumSize(new java.awt.Dimension(45, 45));
        jspPreco.setPreferredSize(new java.awt.Dimension(45, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(jspPreco, gridBagConstraints);

        lblPreco.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblPreco.setForeground(new java.awt.Color(219, 219, 219));
        lblPreco.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblPreco.setText("Preço: R$");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(lblPreco, gridBagConstraints);

        btnAlt.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnAlt.setText("Alterar");
        btnAlt.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnAlt.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        btnAlt.setMinimumSize(new java.awt.Dimension(45, 45));
        btnAlt.setPreferredSize(new java.awt.Dimension(45, 45));
        btnAlt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(btnAlt, gridBagConstraints);

        btnExc.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnExc.setText("Excluir");
        btnExc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnExc.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        btnExc.setMinimumSize(new java.awt.Dimension(45, 45));
        btnExc.setPreferredSize(new java.awt.Dimension(45, 45));
        btnExc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnBaixo.add(btnExc, gridBagConstraints);

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
        consultarMenu();
        procedAjustaTable();
    }//GEN-LAST:event_btnAttActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void tblMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMenuMousePressed
        tblMenu.editingCanceled(null);
        tblMenu.editingStopped(null);
    }//GEN-LAST:event_tblMenuMousePressed

    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultarMenu();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    private void txtPesquisarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyTyped
        String numeros = "0987654321";
        if (numeros.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
        consultarMenu();
    }//GEN-LAST:event_txtPesquisarKeyTyped

    private void tblMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMenuMouseClicked
        setarCampos();
        // TODO add your handling code here:
    }//GEN-LAST:event_tblMenuMouseClicked

    private void AtxtNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtNomeKeyReleased

    }//GEN-LAST:event_AtxtNomeKeyReleased

    private void AtxtNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtNomeKeyTyped

    }//GEN-LAST:event_AtxtNomeKeyTyped

    private void btnAltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltActionPerformed
        alterar_menu();
    }//GEN-LAST:event_btnAltActionPerformed

    private void AtxtObsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtObsKeyReleased

    }//GEN-LAST:event_AtxtObsKeyReleased

    private void AtxtObsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AtxtObsKeyTyped

    }//GEN-LAST:event_AtxtObsKeyTyped

    private void btnExcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcActionPerformed
        remover();
    }//GEN-LAST:event_btnExcActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AtxtNome;
    private javax.swing.JTextArea AtxtObs;
    private javax.swing.JButton btnAlt;
    private javax.swing.JButton btnAtt;
    private javax.swing.JButton btnExc;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JComboBox<String> cboTipo;
    private javax.swing.JScrollPane jspMenu;
    private javax.swing.JScrollPane jspNome;
    private javax.swing.JScrollPane jspObs;
    private javax.swing.JSpinner jspPreco;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblObs;
    private javax.swing.JLabel lblPesquisa;
    private javax.swing.JLabel lblPreco;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lbl_Id;
    private javax.swing.JPanel pnBaixo;
    private javax.swing.JPanel pnCima;
    private javax.swing.JPanel pnMeio;
    private javax.swing.JPanel pnPrincipal;
    private javax.swing.JTable tblMenu;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JTextField txt_Id;
    // End of variables declaration//GEN-END:variables
}
