package Funcoes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
// chama o arquivo de conexao com o banco de dados
import Connection.ConnectionClass;
import java.awt.Color;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author DIOGO
 */
public class Verifica_Validacao {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public static boolean verificarEmail(String email) {
        // Verifica se o email contém o caractere "@"
        if (!email.contains("@")) {
            return false;
        }

        // Verifica se o email contém o ".com"
        if (!email.contains(".com")) {
            return false;
        }

        // Verifica se o "@" não é o primeiro caractere
        if (email.indexOf("@") == 0) {
            return false;
        }

        // Verifica se o último ponto vem depois do "@"
        if (email.lastIndexOf(".") < email.indexOf("@")) {
            return false;
        }

        // Verifica se o último ponto não é o último caractere
        if (email.lastIndexOf(".") == email.length() - 1) {
            return false;
        }

        return true;
    }

    public String verificaDataNasc(String a) {
        String aux = "";

        System.out.println("String a:" + a);

        if (a.contains("-")) {
            String[] partes = a.trim().split("-");

            String dia = partes[2];
            System.out.println("Dia: " + dia);
            String mes = partes[1];
            System.out.println("Mês: " + mes);
            String ano = partes[0];
            System.out.println("Ano: " + ano);

            // Formata a data no formato MM/DD/YYYY
            aux = String.format("%02d/%02d/%04d",
                    Integer.parseInt(dia),
                    Integer.parseInt(mes),
                    Integer.parseInt(ano));
            // Formata a data no formato MM/DD/YYYY
            aux = aux.replace('-', '/');
            System.out.println("aux:" + aux);
            return aux;
        }
        if (a.contains("/")) {
            String[] partes = a.trim().split("/");

            String dia = partes[0];
            System.out.println("Dia: " + dia);
            String mes = partes[1];
            System.out.println("Mês: " + mes);
            String ano = partes[2];
            System.out.println("Ano: " + ano);

            // Formata a data no formato YYYY/MM/DD
            aux = String.format("%04d/%02d/%02d",
                    Integer.parseInt(ano),
                    Integer.parseInt(mes),
                    Integer.parseInt(dia));
            System.out.println("aux format:" + aux);
            // Formata a data no formato YYYY-MM-DD
            aux = aux.replace('/', '-');
            System.out.println("aux:" + aux);

            return aux;
        }
        System.out.println("aux:" + aux);
        return aux;
    }

    public String verificarSexo(String a) {
        String aux = "";
        if ("M".equals(a)) {
            aux = "Masculino";
            return aux;
        } else if ("F".equals(a)) {
            aux = "Feminino";
            return aux;
        } else if ("O".equals(a)) {
            aux = "Outros";
            return aux;
        } else if ("Masculino".equals(a)) {
            aux = "M";
            return aux;
        } else if ("Feminino".equals(a)) {
            aux = "F";
            return aux;
        } else if ("Outros".equals(a)) {
            aux = "O";
            return aux;
        }
        return aux;
    }

    public static boolean validaDataNasc(String dataNascimento) {
        // Define o formato esperado para a data de nascimento
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        //.setLenient ele permitirá que a análise seja flexível e aceite datas que
        //não estão dentro dos limites do calendário. Se for definido
        //como falso, a análise será rigorosa e só aceitará datas válidas
        //dentro do calendário.
        sdf.setLenient(false); // Define que a validação deve ser rigorosa

        try {
            // Faz o parsing da data de nascimento para converter uma string em uma data.
            // String -> Date
            Date data = sdf.parse(dataNascimento);

            // Verifica se a data de nascimento é anterior à data atual
            Calendar calDataNascimento = Calendar.getInstance();
            calDataNascimento.setTime(data);
            // pega a data de hoje
            Calendar calDataAtual = Calendar.getInstance();
            calDataAtual.setTime(new Date());
            Date dataLimite = sdf.parse("01/01/1900");

            //.before é usado para comparar duas datas ou horas para verificar se a 
            //primeira é anterior à segunda. 
            if (calDataNascimento.before(calDataAtual) && data.after(dataLimite)) {
                // se data digitada for anterior a data atual, retorna true
//                System.out.println("before: " + data);
                return true;
            }
            if (calDataNascimento.after(calDataAtual)) {
                // se data digitada for depois da data atual, retorna false
//                System.out.println("after");
                return false;
            }
//            System.out.println("before: " + data);
            return false;

        } catch (ParseException e) {
            // Se houver erro no parsing, a data não é válida
            return false;
        }
    }

    public static boolean validaTel(String tel) {
        tel = tel.replaceAll("[^0-9]", ""); // remove caracteres não numéricos

        if (tel.length() != 11) // verifica se o tamanho do CPF é igual a 11
        {
            return false;
        }

        if (tel.equals("00000000000")
                || tel.equals("11111111111")
                || tel.equals("22222222222")
                || tel.equals("33333333333")
                || tel.equals("44444444444")
                || tel.equals("55555555555")
                || tel.equals("66666666666")
                || tel.equals("77777777777")
                || tel.equals("88888888888")
                || tel.equals("99999999999")) {
            return false;
        }

        return true;
    }

    public static boolean validaCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", ""); // remove caracteres não numéricos

        if (cpf.length() != 11) // verifica se o tamanho do CPF é igual a 11
        {
            return false;
        }

        if (cpf.equals("00000000000")
                || cpf.equals("11111111111")
                || cpf.equals("22222222222")
                || cpf.equals("33333333333")
                || cpf.equals("44444444444")
                || cpf.equals("55555555555")
                || cpf.equals("66666666666")
                || cpf.equals("77777777777")
                || cpf.equals("88888888888")
                || cpf.equals("99999999999")) {
            return false;
        }

        int soma = 0;
        int resto = 0;
        // realiza o calculo do primeiro digito verificador
        for (int i = 1; i <= 9; i++) {
//            System.out.println("antes i: " + i + "| soma: " + soma);
            soma += Integer.parseInt(cpf.substring(i - 1, i)) * (11 - i);
//            System.out.println("depois i: " + i + "| soma: " + soma);
        }
        // esse calculo precisa trazer o primeiro digito verificador digitado
        resto = (soma * 10) % 11;
//        System.out.println("resto: " + resto);
        if (resto == 10 || resto == 11) {
            resto = 0;
        }
        if (resto != Integer.parseInt(cpf.substring(9, 10))) {
            return false;
        }

        soma = 0;
        // realiza o calculo do segundo digito verificador
        for (int i = 1; i <= 10; i++) {
            soma += Integer.parseInt(cpf.substring(i - 1, i)) * (12 - i);
        }
        // esse calculo precisa trazer o segundo digito verificador digitado
        resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) {
            resto = 0;
        }
        if (resto != Integer.parseInt(cpf.substring(10))) {
            return false;
        }

        return true;

    }

    public static boolean validaRG(String rg) {
        rg = rg.replaceAll("[^0-9]", ""); // remove caracteres não numéricos

        if (rg.length() != 9 || rg.contains(" ") || rg.length() > 9) // verifica se o tamanho do RG é igual a 9
        {
            return false;
        }

        if (rg.equals("000000000")
                || rg.equals("111111111")
                || rg.equals("222222222")
                || rg.equals("333333333")
                || rg.equals("444444444")
                || rg.equals("555555555")
                || rg.equals("666666666")
                || rg.equals("777777777")
                || rg.equals("888888888")
                || rg.equals("999999999")) {
            return false;
        }

        // Calcula o dígito verificador
        int soma = 0;
        for (int i = 0; i < 8; i++) {
            int num = Character.getNumericValue(rg.charAt(i));
            soma += num * (2 + i);
        }
        int dv = 11 - (soma % 11);
        if (dv == 10 || dv == 11) {
            dv = 0;
        }

        // Verifica se o dígito verificador é válido
        int digito = Character.getNumericValue(rg.charAt(8));

        return digito == dv;
    }

    public static boolean validaCNPJ(String cnpj) {
        // Remover pontos e traços, se houver
        cnpj = cnpj.replace(".", "").replace("-", "").replace("/", "");

        if (cnpj.equals("00000000000000")
                || cnpj.equals("11111111111111")
                || cnpj.equals("22222222222222")
                || cnpj.equals("33333333333333")
                || cnpj.equals("44444444444444")
                || cnpj.equals("55555555555555")
                || cnpj.equals("66666666666666")
                || cnpj.equals("77777777777777")
                || cnpj.equals("88888888888888")
                || cnpj.equals("99999999999999")) {
            return false;
        }

        // Verifica se o CNPJ tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os caracteres são dígitos numéricos
        for (int i = 0; i < 14; i++) {
            if (!Character.isDigit(cnpj.charAt(i))) {
                return false;
            }
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        int peso = 5;
        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * peso;
            peso--;
            if (peso == 1) {
                peso = 9;
            }
        }
        int resto = soma % 11;
        int digito1 = resto < 2 ? 0 : 11 - resto;

        // Calcula o segundo dígito verificador
        soma = 0;
        peso = 6;
        for (int i = 0; i < 13; i++) {
            soma += (cnpj.charAt(i) - '0') * peso;
            peso--;
            if (peso == 1) {
                peso = 9;
            }
        }
        resto = soma % 11;
        int digito2 = resto < 2 ? 0 : 11 - resto;

        // Verifica se os dígitos verificadores calculados são iguais aos informados
        return (cnpj.charAt(12) - '0') == digito1 && (cnpj.charAt(13) - '0') == digito2;
    }

    public static boolean validaCep(String cep) {
        cep = cep.replaceAll("[^0-9]", "");

        // verifica se o tamanho do CEP é igual a 8
        if (cep.length() != 8
                || cep.contains(" ")
                || cep.length() > 8) {
            return false;
        }

        if (cep.equals("00000000")
                || cep.equals("11111111")
                || cep.equals("22222222")
                || cep.equals("33333333")
                || cep.equals("44444444")
                || cep.equals("55555555")
                || cep.equals("66666666")
                || cep.equals("77777777")
                || cep.equals("88888888")
                || cep.equals("99999999")) {
            return false;
        }

        return true;
    }

    public String validaSenha() {
        int tamanho = 15; // Tamanho da senha
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789";
        Random rand = new Random();

        StringBuilder senha = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            int posicao = rand.nextInt(caracteres.length());
            senha.append(caracteres.charAt(posicao));
        }
        //GERAR CRIPTOGRAFIA PARA O BD
        return senha.toString();
    }

    public boolean verificadorSenha(String senha, String senhaRep) {
        
        boolean verificarComprimento = false;
        boolean verificarLetraMaiuscula = false;
        boolean verificarLetraMinuscula = false;
        boolean verificarCaracterEspecial = false;
        boolean verificarNumero = false;
        boolean verificarSenhasIguais = false;

        if (senha.length() >= 4 && senha.length() <= 12) {
            verificarComprimento = true;
        }

        for (char c : senha.toCharArray()) {
            if (Character.isUpperCase(c)) {
                verificarLetraMaiuscula = true;
            }

        }

        for (char c : senha.toCharArray()) {
            if (Character.isLowerCase(c)) {
                verificarLetraMinuscula = true;
            }
        }

        String caracteresEspeciais = "!@#$%^&*()-_=+[{]};:',<.>/?";
        for (char c : senha.toCharArray()) {
            if (caracteresEspeciais.contains(Character.toString(c))) {
                verificarCaracterEspecial = true;
            }
        }

        for (char c : senha.toCharArray()) {
            if (Character.isDigit(c)) {
                verificarNumero = true;
            }
        }

        verificarSenhasIguais = senha.equals(senhaRep);

        if (verificarComprimento
                && verificarLetraMaiuscula
                && verificarLetraMinuscula
                && verificarCaracterEspecial
                && verificarNumero
                && verificarSenhasIguais) {

            return true;
        } else {

            return false;
        }

    }

}
