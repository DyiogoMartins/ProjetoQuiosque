/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.sql.*;
import Connection.ConnectionClass;
import Connection.Links;
import Funcoes.Verifica_Validacao;
import java.awt.Color;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diogo e eric
 */
public class Tela_Login extends javax.swing.JFrame {

    //VARIAVEIS DE BANCO DE DADOS
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    // VARIAVEIS PARA DATA
    String dia = "";
    String mes = "";
    String ano = "";
    String cod_mes_nome = null;

    //VARIAVEIS PARA O HORARIO
    String hora = "";
    String minuto = "";
    String segundo = "";

    String IDRec = "";
    boolean verifSenha = false;

    // FUNCAO QUE ESTABELECE A HORA E DATA CORRETA
    private void HORA_DATA() {
        Date dataSistema = new Date();
        SimpleDateFormat formato_dia = new SimpleDateFormat("dd");
        SimpleDateFormat formato_mes = new SimpleDateFormat("MM");
        SimpleDateFormat formato_ano = new SimpleDateFormat("yyyy");
        lblDia.setText(formato_dia.format(dataSistema));
        lblMes.setText(formato_mes.format(dataSistema));
        lblAno.setText(formato_ano.format(dataSistema));
        Timer timer = new Timer(0, new hora_sistema());
        timer.start();
    }

    // FUNCAO QUE PEGA A DATA E HORA  
    private void Data_Hora_Agora() {
        dia = lblDia.getText();
        mes = lblMes.getText();
        ano = lblAno.getText();
        hora = lblHora.getText();
        minuto = lblMinuto.getText();
        segundo = lblSegundo.getText();
    }

    // FUNCAO QUE VERIFICA O ACESSO DIARIO DO USUARIO E SE HA ERRO 
    //DE HORARIO CASO TENHA TENTATIVA DE FRAUDE NO SISTEMA
    private void VerificacaoDeAcessoDia() {

        Data_Hora_Agora();

        String sql2 = "select Data_Hora from tb_acessos where Data_Hora like '%"
                + ano + "-"
                + mes + "-"
                + dia + " "
                + hora + ":"
                + minuto + ":"
                + segundo
                + "%'";
        try {

            pst = conexao.prepareStatement(sql2);
            rs = pst.executeQuery();

            if (rs.next()) {

                System.out.println("Consta no banco");
                System.out.println("Data Sistema:\n "
                        + dia + "-"
                        + mes + "-"
                        + ano + " "
                        + hora + ":"
                        + minuto + ":"
                        + segundo);

                CadastroDeAcesso();
            } else {

                System.out.println("Não consta no banco");
                System.out.println("Data Sistema:\n "
                        + dia + "-"
                        + mes + "-"
                        + ano + " "
                        + hora + ":"
                        + minuto + ":"
                        + segundo);
                CadastroDeAcesso();

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);
        }

    }

    // FUNCAO QUE REGISTRA A ENTRADA DOS USUARIOS NO SISTEMA:
    //LOGIN, ENTRADA OU SAIDA, DATA E HORA.
    private void CadastroDeAcesso() {

        Data_Hora_Agora();

        String idFunc = getFuncionario(txtLogin.getText());

        String sql = "insert into tb_Acessos( "
                + " tb_funcionario_idFunc, EntradaOuSaida, Data_Hora)"
                + " values(?, ?, ?)";
        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, idFunc);
            pst.setString(2, "Entrada");
            pst.setString(3, ano + "-" + mes + "-" + dia + " "
                    + hora + ":" + minuto + ":" + segundo);

            if ((idFunc.isEmpty())) {

                //JOptionPane.showMessageDialog(null, "Sua Data ou Login não consta no sistema! Para mais informações, entre em contato.");
            } else {

                int adicionado = pst.executeUpdate();
                System.out.println("Adicionado : " + adicionado);

                if (adicionado > 0) {
                    //JOptionPane.showMessageDialog(null, "Data de acesso registrado!!");
                    Logar();

                }

            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);
        }

    }

    private String getFuncionario(String login) {

        String idFunc = "";

        String sql = "select idFunc as 'ID'"
                + " from tb_funcionario where login like ?";
        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, login);
            rs = pst.executeQuery();

            while (rs.next()) {
                idFunc = rs.getString("ID");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "getFuncionario: \n" + e);
        }
        return idFunc;
    }

    private void Logar() {
        // FUNCAO PARA VERIFICAR E LIBERAR O ACESSO A TELA PRINCIPAL DO SISTEMA

        // PESQUISA NO BANCO DE DADOS O LOGIN E SENHA SIGITADO PELO USUARIO
        String sql = "select * from tb_funcionario"
                + " where login=?"
                + " and AES_DECRYPT(senha, 'encryption_key') = ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtLogin.getText());
            pst.setString(2, txtSenha.getText());
            // ARMAZENA OS DADOS COLETADOS DA PESQUISA
            rs = pst.executeQuery();

            if (rs.next()) {
                // ARMAZENA NA VARIAVEL perfil O DADO DA COLUNA 11 DO REGISTRO
                String perfil = rs.getString(11);
                String situacao = rs.getString(18);
                //System.out.println("PERFIL:" + perfil);

                if (situacao.equals("Desligado")) {

                    JOptionPane.showMessageDialog(null, "Usuario não tem mais acesso ao sistema: " + txtLogin.getText());

                } else if (situacao.equals("Afastado")) {

                    JOptionPane.showMessageDialog(null, "Usuario não tem mais acesso ao sistema: " + txtLogin.getText());

                } else {

                    JOptionPane.showMessageDialog(null, "Bem Vindo: " + txtLogin.getText());
                    // CONDICIONAL QUE VERIFICA QUAL CARGO HIERARQUICO DO LOGIN TERA AUTORIZAÇÃO
                    //PARA MEXER DENTRO DO SISTEMA
                    if (perfil.equals("Administrador")) {
                        JOptionPane.showMessageDialog(null, "Acesso Concedido!");
                        // ABRE A TELA PRINCIPAL DO SISTEMA 
                        new Tela_Principal().setVisible(true);
                        Tela_Principal.jmFunc.setEnabled(true);
                        Tela_Principal.jmFuncCad.setEnabled(true);
                        Tela_Principal.jmFuncAlt.setEnabled(true);
                        Tela_Principal.jmFuncVil.setEnabled(true);

                        Tela_Principal.jmEstCad.setEnabled(true);
                        Tela_Principal.jmEstAlt.setEnabled(true);
                        Tela_Principal.jmEstGasto.setEnabled(true);

                        Tela_Principal.jmForn.setEnabled(true);
                        Tela_Principal.jmFornCli.setEnabled(true);
                        Tela_Principal.jmFornCad.setEnabled(true);
                        Tela_Principal.jmFornAlt.setEnabled(true);

                        Tela_Principal.jmMenuCad.setEnabled(true);
                        Tela_Principal.jmMenuAlt.setEnabled(true);

                        Tela_Principal.jmRel.setEnabled(true);

                        Tela_Principal.jmCliAlt.setEnabled(true);

                        // SETA NA TELA PRINCIPAL O NOME, CARGO E SETOR PARA
                        //FUNCOES DE OUTRAS TELAS
                        Tela_Principal.lblLogin.setText(rs.getString(5));
                        Tela_Principal.lblCargo.setText(rs.getString(11));
                        Tela_Principal.lblSetor.setText(rs.getString(12));
                        this.dispose();

                    } else if (perfil.equals("Gerente")) {
                        JOptionPane.showMessageDialog(null, "Acesso Concedido!");
                        new Tela_Principal().setVisible(true);
                        Tela_Principal.jmFunc.setEnabled(true);
                        Tela_Principal.jmFuncCad.setEnabled(true);
                        Tela_Principal.jmFuncAlt.setEnabled(true);
                        Tela_Principal.jmFuncVil.setEnabled(true);

                        Tela_Principal.jmEstCad.setEnabled(true);
                        Tela_Principal.jmEstAlt.setEnabled(true);
                        Tela_Principal.jmEstGasto.setEnabled(true);

                        Tela_Principal.jmForn.setEnabled(true);
                        Tela_Principal.jmFornCli.setEnabled(true);
                        Tela_Principal.jmFornCad.setEnabled(true);
                        Tela_Principal.jmFornAlt.setEnabled(true);

                        Tela_Principal.jmMenuCad.setEnabled(true);
                        Tela_Principal.jmMenuAlt.setEnabled(true);

                        Tela_Principal.jmRel.setEnabled(true);

                        Tela_Principal.jmCliAlt.setEnabled(true);

                        Tela_Principal.lblLogin.setText(rs.getString(5));
                        Tela_Principal.lblCargo.setText(rs.getString(11));
                        Tela_Principal.lblSetor.setText(rs.getString(12));
                        this.dispose();

                    } else if (perfil.equals("Supervisor")) {//                               
                        new Tela_Principal().setVisible(true);

                        Tela_Principal.jmFunc.setEnabled(true);
                        Tela_Principal.jmFuncCad.setEnabled(true);
                        Tela_Principal.jmFuncVil.setEnabled(true);

                        Tela_Principal.jmEstCad.setEnabled(true);
                        Tela_Principal.jmEstAlt.setEnabled(true);
                        Tela_Principal.jmEstGasto.setEnabled(true);

                        Tela_Principal.jmForn.setEnabled(true);
                        Tela_Principal.jmFornCli.setEnabled(true);
                        Tela_Principal.jmFornCad.setEnabled(true);

                        Tela_Principal.jmMenuCad.setEnabled(true);

                        Tela_Principal.jmRel.setEnabled(true);

                        Tela_Principal.jmCliAlt.setEnabled(true);

                        Tela_Principal.lblLogin.setText(rs.getString(5));
                        Tela_Principal.lblCargo.setText(rs.getString(11));
                        Tela_Principal.lblSetor.setText(rs.getString(12));
                        this.dispose();

                    } else if (perfil.equals("Balconista")) {
                        new Tela_Principal().setVisible(true);

                        Tela_Principal.lblLogin.setText(rs.getString(5));
                        Tela_Principal.lblCargo.setText(rs.getString(11));
                        Tela_Principal.lblSetor.setText(rs.getString(12));
                        this.dispose();

                    } else if (perfil.equals("Atendente")) {
                        new Tela_Principal().setVisible(true);

                        Tela_Principal.lblLogin.setText(rs.getString(5));
                        Tela_Principal.lblCargo.setText(rs.getString(11));
                        Tela_Principal.lblSetor.setText(rs.getString(12));
                        this.dispose();
                    }

                    //*new TelaPrincipal().setVisible(true);
                }
            } else {
                // CONDICIONAL QUE INFORMA AO USUARIO QUE O LOGIN OU SENHA ESTAO ERRADOS
                JOptionPane.showMessageDialog(null, "Login ou Senha incorreta", "Acesso negado",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void VerificaDadosTrocaSenha() {

        String sql = "select idFunc, nomeFunc as Nome, CPF, DataDeNasc as Data_de_Nascimento \n"
                + "from tb_funcionario where nomeFunc like ? and CPF like ? \n"
                + "and DataDeNasc like ?";

        // DateTimeFormatter Formatador para impressão e análise de objetos de data e hora.
        // .ofPattern Cria um formatador usando o padrão especificado.
        //DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // .parse converter de String para Date
        // o padrao de impressao do LocalDate é yyyy/MM/dd
        //LocalDate data = LocalDate.parse(txtDataNascRec.getText(), formato);
        //System.out.println(data);
        //para imprimir formato BR é com o .format
        System.out.println(txtDataNascRec.getText());

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeRec.getText() + "%");
            pst.setString(2, txtCPFRec.getText() + "%");
            pst.setString(3, funcaoDataNasc(txtDataNascRec.getText()) + "%");
            rs = pst.executeQuery();

            // CONDICIONAL QUE VERIFICA SE OS CAMPOS FORAM PREENCHIDOS ANTES DE VERIFICAR NO BANCO
            if ((txtNomeRec.getText().isEmpty())
                    || (txtCPFRec.getText().isEmpty())
                    || (txtDataNascRec.getText().isEmpty())) {

                // CONDICIONAL QUE INFORMA AO USUARIO QUE EXISTEM CAMPOS EM BANCO
                JOptionPane.showMessageDialog(null,
                        "Existem campos em branco, favor preencher todos!");

                // CONDICIONAL QUE LIBERA AO USUARIO PROCEGUIR COM A SOLICTACAO
            } else {

                if (rs.next()) {

                    IDRec = rs.getString(1);
//                    String nomeRec = rs.getString(2);
//                    String CPFRec = rs.getString(3);
//                    String DataNascRec = rs.getString(4);
//
//                   System.out.println(IDRec + "\n" + nomeRec + "\n" + CPFRec + "\n" + DataNascRec);

                    pnTrocaSenha2.setVisible(true);
                    pnTrocaSenha.setVisible(false);

                } else {
                    // CONDICIONAL QUE INFORMA AO USUARIO QUE OS DADOS NAO COINCIDEM
                    JOptionPane.showMessageDialog(null,
                            "Dados nao coindidem, favor tentar novamente!",
                            "Acesso negado",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e + "NENHUM REGISTRO ENCONTRADO");
        }

    }

    private void AtualizaSenhaNova() {
        funcSenha();
        if (verifSenha) {
            // se verdadeiro ele deixa trocar a senha

            String sql2 = "update tb_funcionario"
                    + " set senha = AES_ENCRYPT(?, 'encryption_key')"
                    + " where idFunc = ?";

            try {

                pst = conexao.prepareStatement(sql2);
                pst.setString(1, txtNovaSenha.getText());
                pst.setString(2, IDRec.toString());

                if ((txtNovaSenha.getText().isEmpty())
                        || (txtRepNovaSenha.getText().isEmpty())) {

                    JOptionPane.showMessageDialog(null,
                            "Existem campos em branco, favor preencher todos!");

                } else {

                    int adicionado = pst.executeUpdate();
                    System.out.println(adicionado);

                    if (adicionado > 0) {
                        JOptionPane.showMessageDialog(null,
                                "Senha do usuário alterados com sucesso!");

                        txtNovaSenha.setText(null);
                        txtRepNovaSenha.setText(null);
                        pnTrocaSenha2.setVisible(false);
                        pnTrocaSenha.setVisible(false);
                        pnLogin.setVisible(true);
                        txtNomeRec.setText(null);
                        txtDataNascRec.setText(null);
                        txtCPFRec.setText(null);

                    } else {
                        JOptionPane.showMessageDialog(null,
                                "ID(Identificação) só aceita números validos!",
                                "Atenção!",
                                HEIGHT);
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        e + "Nenhum registro encontrado!");
            }
        } else {
            // se falso, ele nao permite trocar a senha
            JOptionPane.showMessageDialog(null,
                    "A senha não atende as exigencias");
        }

    }

    private void funcSenha() {

        Verifica_Validacao funcoes = new Verifica_Validacao();
        verifSenha = funcoes.verificadorSenha(txtNovaSenha.getText(), txtRepNovaSenha.getText());

    }

    private String funcaoDataNasc(String a) {
        Verifica_Validacao funcoes = new Verifica_Validacao();
        String verificacao = funcoes.verificaDataNasc(a);
        return verificacao;
    }

    /**
     * Creates new form TelaLogin
     */
    public Tela_Login() {
        // FUNCAO INICIAL DA TELA PARA VERIFICAR SE HA CONEXAO COM O BD E SE A DATA E HORA ESTAO CERTOS

        initComponents();
        conexao = ConnectionClass.conector();
        pnTrocaSenha.setVisible(false);
        pnTrocaSenha2.setVisible(false);
        pnSobre.setVisible(false);

        HORA_DATA();

        if (conexao != null) {

            lblIconBD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/IconBase.png")));
            lblStatus.setText("Conectado");

        } else {

            lblIconBD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/IconBase2.png")));
            lblStatus.setText("Não conectado");

        }

        //this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/imagens/logo-vault-icone2.png")).getImage());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnPrincipal = new javax.swing.JPanel();
        lblVersao = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        lblDoisPontos1 = new javax.swing.JLabel();
        lblMinuto = new javax.swing.JLabel();
        lblDoisPontos2 = new javax.swing.JLabel();
        lblSegundo = new javax.swing.JLabel();
        lblLinha = new javax.swing.JLabel();
        lblDia = new javax.swing.JLabel();
        lblBarra1 = new javax.swing.JLabel();
        lblMes = new javax.swing.JLabel();
        lblBarra2 = new javax.swing.JLabel();
        lblAno = new javax.swing.JLabel();
        lblDesign = new javax.swing.JLabel();
        pnLogin = new javax.swing.JPanel();
        txtLogin = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        lblUsuario = new javax.swing.JLabel();
        lblSenha = new javax.swing.JLabel();
        lblEsqSenha = new javax.swing.JLabel();
        lblIconUsu = new javax.swing.JLabel();
        lblIconSen = new javax.swing.JLabel();
        btnAcessar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        pnTrocaSenha2 = new javax.swing.JPanel();
        txtNovaSenha = new javax.swing.JPasswordField();
        txtRepNovaSenha = new javax.swing.JPasswordField();
        btnLimparNovaS = new javax.swing.JButton();
        btnTrocarSenha = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        lblNovaSenha = new javax.swing.JLabel();
        lblRepNovaSenha = new javax.swing.JLabel();
        lblRegrasSenha = new javax.swing.JLabel();
        lblRegrasSenha2 = new javax.swing.JLabel();
        lblCorriSen = new javax.swing.JLabel();
        pnTrocaSenha = new javax.swing.JPanel();
        txtDataNascRec = new javax.swing.JFormattedTextField();
        txtCPFRec = new javax.swing.JFormattedTextField();
        txtNomeRec = new javax.swing.JTextField();
        btnLimparConfirDdos = new javax.swing.JButton();
        btnEnviarConfirDdos = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        lblDataNasc = new javax.swing.JLabel();
        lblNomeCom = new javax.swing.JLabel();
        lblCPF = new javax.swing.JLabel();
        btnVoltarLogin = new javax.swing.JButton();
        lblConfDados = new javax.swing.JLabel();
        lblLogin = new javax.swing.JLabel();
        lblSobre = new javax.swing.JLabel();
        pnSobre = new javax.swing.JPanel();
        lblIconBD = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblIconTel = new javax.swing.JLabel();
        lblIconFace = new javax.swing.JLabel();
        lblIconMail = new javax.swing.JLabel();
        lblIconInst = new javax.swing.JLabel();
        lblTel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tela Login");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnPrincipal.setBackground(new java.awt.Color(51, 51, 51));
        pnPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblVersao.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        lblVersao.setText("Vault 13.0.0");
        pnPrincipal.add(lblVersao, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 80, -1));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/gif-logo.gif"))); // NOI18N
        pnPrincipal.add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 220, 230));

        lblHora.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblHora.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHora.setText("12");
        pnPrincipal.add(lblHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 25, 20));

        lblDoisPontos1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblDoisPontos1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoisPontos1.setText(":");
        pnPrincipal.add(lblDoisPontos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 260, 10, 20));

        lblMinuto.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblMinuto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMinuto.setText("32");
        pnPrincipal.add(lblMinuto, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 25, 20));

        lblDoisPontos2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblDoisPontos2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoisPontos2.setText(":");
        pnPrincipal.add(lblDoisPontos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 260, 10, 20));

        lblSegundo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblSegundo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSegundo.setText("22");
        pnPrincipal.add(lblSegundo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 25, 20));

        lblLinha.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblLinha.setText("______________________");
        pnPrincipal.add(lblLinha, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 268, -1, -1));

        lblDia.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblDia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDia.setText("08");
        pnPrincipal.add(lblDia, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 285, 30, 30));

        lblBarra1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblBarra1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarra1.setText("/");
        pnPrincipal.add(lblBarra1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 10, 40));

        lblMes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblMes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMes.setText("03");
        pnPrincipal.add(lblMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 285, 30, 30));

        lblBarra2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblBarra2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBarra2.setText("/");
        pnPrincipal.add(lblBarra2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 10, 40));

        lblAno.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblAno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAno.setText("2019");
        pnPrincipal.add(lblAno, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 285, 60, 30));

        lblDesign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/background tela de login.png"))); // NOI18N
        pnPrincipal.add(lblDesign, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 450, 340));

        pnLogin.setBackground(new java.awt.Color(51, 51, 51));
        pnLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtLogin.setBackground(new java.awt.Color(51, 51, 51));
        txtLogin.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtLogin.setBorder(null);
        txtLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoginActionPerformed(evt);
            }
        });
        txtLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLoginKeyPressed(evt);
            }
        });
        pnLogin.add(txtLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 250, 20));

        txtSenha.setBackground(new java.awt.Color(51, 51, 51));
        txtSenha.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtSenha.setBorder(null);
        txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSenhaKeyPressed(evt);
            }
        });
        pnLogin.add(txtSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 110, 250, 20));

        lblUsuario.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(204, 204, 204));
        lblUsuario.setText("Usuário");
        pnLogin.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 20, -1, -1));

        lblSenha.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblSenha.setForeground(new java.awt.Color(204, 204, 204));
        lblSenha.setText("Senha");
        pnLogin.add(lblSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 80, -1, -1));

        lblEsqSenha.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        lblEsqSenha.setForeground(new java.awt.Color(211, 199, 170));
        lblEsqSenha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEsqSenha.setText("Esqueceu a senha?");
        lblEsqSenha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEsqSenhaMouseClicked(evt);
            }
        });
        pnLogin.add(lblEsqSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, 140, 20));

        lblIconUsu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/IconP.png"))); // NOI18N
        pnLogin.add(lblIconUsu, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, -1, -1));

        lblIconSen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/padlock.png"))); // NOI18N
        pnLogin.add(lblIconSen, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 80, -1, -1));

        btnAcessar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnAcessar.setText("Acessar");
        btnAcessar.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnAcessar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcessarActionPerformed(evt);
            }
        });
        btnAcessar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAcessarKeyPressed(evt);
            }
        });
        pnLogin.add(btnAcessar, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 210, 110, 50));

        btnLimpar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        pnLogin.add(btnLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 210, 110, 50));
        pnLogin.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 270, 10));
        pnLogin.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 130, 270, 10));

        pnPrincipal.add(pnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 720, 270));

        pnTrocaSenha2.setBackground(new java.awt.Color(51, 51, 51));
        pnTrocaSenha2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNovaSenha.setBackground(new java.awt.Color(51, 51, 51));
        txtNovaSenha.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtNovaSenha.setBorder(null);
        txtNovaSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNovaSenhaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNovaSenhaKeyTyped(evt);
            }
        });
        pnTrocaSenha2.add(txtNovaSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 100, 280, 20));

        txtRepNovaSenha.setBackground(new java.awt.Color(51, 51, 51));
        txtRepNovaSenha.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtRepNovaSenha.setBorder(null);
        txtRepNovaSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRepNovaSenhaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRepNovaSenhaKeyTyped(evt);
            }
        });
        pnTrocaSenha2.add(txtRepNovaSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 280, 20));

        btnLimparNovaS.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLimparNovaS.setText("LIMPAR");
        btnLimparNovaS.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnLimparNovaS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparNovaSActionPerformed(evt);
            }
        });
        btnLimparNovaS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLimparNovaSKeyPressed(evt);
            }
        });
        pnTrocaSenha2.add(btnLimparNovaS, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 260, 110, 50));

        btnTrocarSenha.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnTrocarSenha.setText("TROCAR");
        btnTrocarSenha.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnTrocarSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrocarSenhaActionPerformed(evt);
            }
        });
        btnTrocarSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTrocarSenhaKeyPressed(evt);
            }
        });
        pnTrocaSenha2.add(btnTrocarSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 260, 110, 50));
        pnTrocaSenha2.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, 300, 10));
        pnTrocaSenha2.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 180, 300, 10));

        lblNovaSenha.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblNovaSenha.setForeground(new java.awt.Color(204, 204, 204));
        lblNovaSenha.setText("Nova Senha");
        pnTrocaSenha2.add(lblNovaSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, -1, -1));

        lblRepNovaSenha.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblRepNovaSenha.setForeground(new java.awt.Color(204, 204, 204));
        lblRepNovaSenha.setText("Repetir Nova Senha");
        pnTrocaSenha2.add(lblRepNovaSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 130, -1, -1));

        lblRegrasSenha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRegrasSenha.setForeground(new java.awt.Color(204, 204, 204));
        lblRegrasSenha.setText("<html>\n[] Comprimento de senha<br>\n(entre 4 - 12 caracteres)<br>\n[] Letra maiúscula<br> \n[] Letra minúscula\n</html> ");
        lblRegrasSenha.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnTrocaSenha2.add(lblRegrasSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 190, 140, 60));

        lblRegrasSenha2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRegrasSenha2.setForeground(new java.awt.Color(204, 204, 204));
        lblRegrasSenha2.setText("<html>\n[] Carácter especial <br> \n[] Número<br> \n[] Senhas iguais\n</html>");
        lblRegrasSenha2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pnTrocaSenha2.add(lblRegrasSenha2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 190, 120, 60));

        lblCorriSen.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblCorriSen.setForeground(new java.awt.Color(204, 204, 204));
        lblCorriSen.setText("Corrigir Senha");
        lblCorriSen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCorriSenMouseClicked(evt);
            }
        });
        lblCorriSen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblCorriSenKeyPressed(evt);
            }
        });
        pnTrocaSenha2.add(lblCorriSen, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, -1, -1));

        pnPrincipal.add(pnTrocaSenha2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 320));

        pnTrocaSenha.setBackground(new java.awt.Color(51, 51, 51));
        pnTrocaSenha.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtDataNascRec.setBackground(new java.awt.Color(51, 51, 51));
        txtDataNascRec.setBorder(null);
        try {
            txtDataNascRec.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataNascRec.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtDataNascRec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDataNascRecMouseClicked(evt);
            }
        });
        txtDataNascRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataNascRecActionPerformed(evt);
            }
        });
        txtDataNascRec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataNascRecKeyPressed(evt);
            }
        });
        pnTrocaSenha.add(txtDataNascRec, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 220, 280, 20));

        txtCPFRec.setBackground(new java.awt.Color(51, 51, 51));
        txtCPFRec.setBorder(null);
        try {
            txtCPFRec.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCPFRec.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtCPFRec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCPFRecMouseClicked(evt);
            }
        });
        txtCPFRec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCPFRecKeyPressed(evt);
            }
        });
        pnTrocaSenha.add(txtCPFRec, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 280, 20));

        txtNomeRec.setBackground(new java.awt.Color(51, 51, 51));
        txtNomeRec.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtNomeRec.setBorder(null);
        pnTrocaSenha.add(txtNomeRec, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 100, 280, 20));
        txtNomeRec.getAccessibleContext().setAccessibleName("");

        btnLimparConfirDdos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnLimparConfirDdos.setText("LIMPAR");
        btnLimparConfirDdos.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnLimparConfirDdos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparConfirDdosActionPerformed(evt);
            }
        });
        btnLimparConfirDdos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLimparConfirDdosKeyPressed(evt);
            }
        });
        pnTrocaSenha.add(btnLimparConfirDdos, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 260, 110, 50));

        btnEnviarConfirDdos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnEnviarConfirDdos.setText("ENVIAR");
        btnEnviarConfirDdos.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnEnviarConfirDdos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarConfirDdosActionPerformed(evt);
            }
        });
        btnEnviarConfirDdos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEnviarConfirDdosKeyPressed(evt);
            }
        });
        pnTrocaSenha.add(btnEnviarConfirDdos, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 260, 110, 50));
        pnTrocaSenha.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, 300, 10));
        pnTrocaSenha.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 180, 300, 10));
        pnTrocaSenha.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 240, 300, 10));

        lblDataNasc.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblDataNasc.setForeground(new java.awt.Color(204, 204, 204));
        lblDataNasc.setText("Data de Nascimento");
        pnTrocaSenha.add(lblDataNasc, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 190, 160, -1));

        lblNomeCom.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblNomeCom.setForeground(new java.awt.Color(204, 204, 204));
        lblNomeCom.setText("Nome Completo");
        pnTrocaSenha.add(lblNomeCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 130, -1));

        lblCPF.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblCPF.setForeground(new java.awt.Color(204, 204, 204));
        lblCPF.setText("CPF");
        pnTrocaSenha.add(lblCPF, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 130, 40, -1));

        btnVoltarLogin.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnVoltarLogin.setText("<- Voltar");
        btnVoltarLogin.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnVoltarLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarLoginActionPerformed(evt);
            }
        });
        btnVoltarLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnVoltarLoginKeyPressed(evt);
            }
        });
        pnTrocaSenha.add(btnVoltarLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 70, 30));

        lblConfDados.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblConfDados.setForeground(new java.awt.Color(204, 204, 204));
        lblConfDados.setText("Confirmar Dados");
        lblConfDados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblConfDadosMouseClicked(evt);
            }
        });
        lblConfDados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblConfDadosKeyPressed(evt);
            }
        });
        pnTrocaSenha.add(lblConfDados, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, -1, -1));

        pnPrincipal.add(pnTrocaSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 320));

        lblLogin.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblLogin.setForeground(new java.awt.Color(219, 219, 219));
        lblLogin.setText("Login");
        lblLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLoginMouseClicked(evt);
            }
        });
        lblLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblLoginKeyPressed(evt);
            }
        });
        pnPrincipal.add(lblLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, -1, -1));

        lblSobre.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblSobre.setForeground(new java.awt.Color(35, 142, 104));
        lblSobre.setText("Sobre");
        lblSobre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSobreMouseClicked(evt);
            }
        });
        lblSobre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblSobreKeyPressed(evt);
            }
        });
        pnPrincipal.add(lblSobre, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, -1, -1));

        pnSobre.setBackground(new java.awt.Color(51, 51, 51));
        pnSobre.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblIconBD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/IconBase2.png"))); // NOI18N
        pnSobre.add(lblIconBD, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, 40, 40));

        lblStatus.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(204, 204, 204));
        lblStatus.setText("Status");
        pnSobre.add(lblStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 70, -1, -1));

        lblIconTel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/phone.png"))); // NOI18N
        pnSobre.add(lblIconTel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, -1, -1));

        lblIconFace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/facebook-icone-icon.png"))); // NOI18N
        lblIconFace.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblIconFaceMouseClicked(evt);
            }
        });
        pnSobre.add(lblIconFace, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 210, -1, -1));

        lblIconMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/gmail.png"))); // NOI18N
        lblIconMail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblIconMailMouseClicked(evt);
            }
        });
        pnSobre.add(lblIconMail, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 210, -1, -1));

        lblIconInst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/6929237_instagram_icon_48px.png"))); // NOI18N
        lblIconInst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblIconInstMouseClicked(evt);
            }
        });
        pnSobre.add(lblIconInst, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 210, -1, -1));

        lblTel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTel.setForeground(new java.awt.Color(204, 204, 204));
        lblTel.setText("(21) 9 9892-4747");
        pnSobre.add(lblTel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 142, -1, 30));

        pnPrincipal.add(pnSobre, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 720, 270));

        getContentPane().add(pnPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 320));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAcessarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcessarActionPerformed
        VerificacaoDeAcessoDia();

    }//GEN-LAST:event_btnAcessarActionPerformed

    private void txtSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSenhaKeyPressed

    }//GEN-LAST:event_txtSenhaKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        HORA_DATA();
    }//GEN-LAST:event_formWindowOpened

    private void txtLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoginActionPerformed

    private void lblEsqSenhaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEsqSenhaMouseClicked

        //FUNCAO DE VERIFICACAO DE NOME COMPLETO, CPF E DATA_DE_NASCIMENTO PARA TROCAR A SENHA
        // CODIGO QUE EXIBE O PAINEL DE TROCA DE SENHA NA TELA DE LOGIN
        pnTrocaSenha.setVisible(true);
        pnLogin.setVisible(false);

    }//GEN-LAST:event_lblEsqSenhaMouseClicked

    private void lblLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLoginMouseClicked
        lblLogin.setForeground(Color.decode("#DBDBDB")); // cor branca     
        lblSobre.setForeground(Color.decode("#238E68")); // cor verde
        pnLogin.setVisible(true);
        pnSobre.setVisible(false);
    }//GEN-LAST:event_lblLoginMouseClicked

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        txtLogin.setText(null);
        txtSenha.setText(null);
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnAcessarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAcessarKeyPressed

    }//GEN-LAST:event_btnAcessarKeyPressed

    private void txtLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLoginKeyPressed

    }//GEN-LAST:event_txtLoginKeyPressed

    private void lblLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblLoginKeyPressed

    }//GEN-LAST:event_lblLoginKeyPressed

    private void btnLimparConfirDdosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparConfirDdosActionPerformed
        txtDataNascRec.setText(null);
        txtNomeRec.setText(null);
        txtCPFRec.setText(null);
    }//GEN-LAST:event_btnLimparConfirDdosActionPerformed

    private void btnLimparConfirDdosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLimparConfirDdosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparConfirDdosKeyPressed

    private void btnEnviarConfirDdosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarConfirDdosActionPerformed
        // CHAMA A FUNCAO QUE VERIFICA SE OS DADOS ESTAO CORRETOR
        VerificaDadosTrocaSenha();
    }//GEN-LAST:event_btnEnviarConfirDdosActionPerformed

    private void btnEnviarConfirDdosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEnviarConfirDdosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEnviarConfirDdosKeyPressed

    private void btnLimparNovaSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparNovaSActionPerformed
        txtNovaSenha.setText(null);
        txtRepNovaSenha.setText(null);
    }//GEN-LAST:event_btnLimparNovaSActionPerformed

    private void btnLimparNovaSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLimparNovaSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimparNovaSKeyPressed

    private void btnTrocarSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrocarSenhaActionPerformed
        AtualizaSenhaNova();
    }//GEN-LAST:event_btnTrocarSenhaActionPerformed

    private void btnTrocarSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTrocarSenhaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTrocarSenhaKeyPressed

    private void btnVoltarLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarLoginActionPerformed
        pnTrocaSenha.setVisible(false);
        pnLogin.setVisible(true);
    }//GEN-LAST:event_btnVoltarLoginActionPerformed

    private void btnVoltarLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnVoltarLoginKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVoltarLoginKeyPressed

    private void lblSobreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSobreMouseClicked
        lblSobre.setForeground(Color.decode("#DBDBDB")); // cor branca
        lblLogin.setForeground(Color.decode("#238E68")); // cor verde 
        pnSobre.setVisible(true);
        pnLogin.setVisible(false);
    }//GEN-LAST:event_lblSobreMouseClicked

    private void lblSobreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblSobreKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSobreKeyPressed

    private void txtCPFRecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCPFRecMouseClicked

    }//GEN-LAST:event_txtCPFRecMouseClicked

    private void txtCPFRecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCPFRecKeyPressed

    }//GEN-LAST:event_txtCPFRecKeyPressed

    private void txtDataNascRecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDataNascRecMouseClicked

    }//GEN-LAST:event_txtDataNascRecMouseClicked

    private void txtDataNascRecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataNascRecKeyPressed

    }//GEN-LAST:event_txtDataNascRecKeyPressed

    private void lblConfDadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblConfDadosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblConfDadosMouseClicked

    private void lblConfDadosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblConfDadosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblConfDadosKeyPressed

    private void lblCorriSenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCorriSenMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCorriSenMouseClicked

    private void lblCorriSenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblCorriSenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCorriSenKeyPressed

    private void txtDataNascRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataNascRecActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataNascRecActionPerformed

    private void lblIconMailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblIconMailMouseClicked
        Links link = new Links();
        link.abrirEnviarEmail();
    }//GEN-LAST:event_lblIconMailMouseClicked

    private void lblIconFaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblIconFaceMouseClicked

        Links link = new Links();
        try {
            link.abrirPerfilFacebook();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Tela_Login.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_lblIconFaceMouseClicked

    private void lblIconInstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblIconInstMouseClicked
        Links link = new Links();
        link.abrirInstagram();
    }//GEN-LAST:event_lblIconInstMouseClicked

    private void txtNovaSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNovaSenhaKeyPressed
        funcSenha();
    }//GEN-LAST:event_txtNovaSenhaKeyPressed

    private void txtRepNovaSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRepNovaSenhaKeyPressed
        funcSenha();
    }//GEN-LAST:event_txtRepNovaSenhaKeyPressed

    private void txtNovaSenhaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNovaSenhaKeyTyped
        funcSenha();
    }//GEN-LAST:event_txtNovaSenhaKeyTyped

    private void txtRepNovaSenhaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRepNovaSenhaKeyTyped
        funcSenha();
    }//GEN-LAST:event_txtRepNovaSenhaKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tela_Login.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_Login.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_Login.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_Login.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                try {

                    Thread.sleep(4500);

                } catch (Exception e) {
                }

                new Tela_Login().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcessar;
    private javax.swing.JButton btnEnviarConfirDdos;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnLimparConfirDdos;
    private javax.swing.JButton btnLimparNovaS;
    private javax.swing.JButton btnTrocarSenha;
    private javax.swing.JButton btnVoltarLogin;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel lblAno;
    private javax.swing.JLabel lblBarra1;
    private javax.swing.JLabel lblBarra2;
    private javax.swing.JLabel lblCPF;
    private javax.swing.JLabel lblConfDados;
    private javax.swing.JLabel lblCorriSen;
    private javax.swing.JLabel lblDataNasc;
    private javax.swing.JLabel lblDesign;
    private javax.swing.JLabel lblDia;
    private javax.swing.JLabel lblDoisPontos1;
    private javax.swing.JLabel lblDoisPontos2;
    private javax.swing.JLabel lblEsqSenha;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblIconBD;
    private javax.swing.JLabel lblIconFace;
    private javax.swing.JLabel lblIconInst;
    private javax.swing.JLabel lblIconMail;
    private javax.swing.JLabel lblIconSen;
    private javax.swing.JLabel lblIconTel;
    private javax.swing.JLabel lblIconUsu;
    private javax.swing.JLabel lblLinha;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblMes;
    private javax.swing.JLabel lblMinuto;
    private javax.swing.JLabel lblNomeCom;
    private javax.swing.JLabel lblNovaSenha;
    private javax.swing.JLabel lblRegrasSenha;
    private javax.swing.JLabel lblRegrasSenha2;
    private javax.swing.JLabel lblRepNovaSenha;
    private javax.swing.JLabel lblSegundo;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblSobre;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTel;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblVersao;
    private javax.swing.JPanel pnLogin;
    private javax.swing.JPanel pnPrincipal;
    private javax.swing.JPanel pnSobre;
    private javax.swing.JPanel pnTrocaSenha;
    private javax.swing.JPanel pnTrocaSenha2;
    private javax.swing.JFormattedTextField txtCPFRec;
    private javax.swing.JFormattedTextField txtDataNascRec;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtNomeRec;
    private javax.swing.JPasswordField txtNovaSenha;
    private javax.swing.JPasswordField txtRepNovaSenha;
    private javax.swing.JPasswordField txtSenha;
    // End of variables declaration//GEN-END:variables

    class hora_sistema implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Calendar now = Calendar.getInstance();
            lblHora.setText(String.format("%1$tH", now));

            lblMinuto.setText(String.format("%1$tM", now));

            lblSegundo.setText(String.format("%1$tS", now));
        }
    }

}
