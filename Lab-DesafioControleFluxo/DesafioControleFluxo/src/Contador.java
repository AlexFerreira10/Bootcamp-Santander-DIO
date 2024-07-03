import exceptions.ParametrosInvalidosException;
import java.util.Scanner;

public class Contador {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Digite o primeiro parâmetro: ");
        int a = Integer.parseInt(sc.nextLine());
        System.out.print("Digite o segundo parâmetro: ");
        int b = Integer.parseInt(sc.nextLine());

        try {
            contar(a, b);
        } catch (ParametrosInvalidosException e) {
            System.out.println("Erro ao contar: " + e.getMessage());
        } finally {
            sc.close();
        }

    }

    static void contar(int a, int b ) throws ParametrosInvalidosException {
        if(a > b){
            throw new ParametrosInvalidosException("O primeiro parâmetro é maior que o segundo!");
        }

        int contagem = b - a;
        for(int i = 1; i <= contagem; i++) {
            System.out.println("Imprimindo o Número: " + i);
        }
    }
}