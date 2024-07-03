package application;

import entities.Iphone;

public class Main {
    public static void main(String[] args) {
        Iphone meuIphone = new Iphone("Preto", "12 Pro");

        // Testando AparelhoTelefonico
        meuIphone.ligar("123456789");
        meuIphone.atender();
        meuIphone.iniciarCorreioVoz();

        // Testando ReprodutorMusical
        meuIphone.tocar();
        meuIphone.pausar();
        meuIphone.selecionarMusica("Minha Música Favorita");

        // Testando NavegadorNaInternet
        meuIphone.exibirPagina("http://www.exemplo.com");
        meuIphone.adicionarNovaAba();
        meuIphone.atualizarPagina();

        // Testando métodos adicionais
        System.out.println("Cor: " + meuIphone.getCor());
        System.out.println("Versão: " + meuIphone.getVersao());
    }

}