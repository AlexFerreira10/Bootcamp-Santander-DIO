import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

         try {
             // Entrada de dados para os atributos da conta
             System.out.print("Digite o número da conta: ");
             int numero = Integer.parseInt(sc.nextLine());

             System.out.print("Digite a agência da conta: ");
             String agencia = sc.nextLine();

             System.out.print("Digite o nome do cliente: ");
             String nomeCliente =  sc.nextLine();

             System.out.print("Digite o saldo da conta: ");
             double saldo = Double.parseDouble(sc.nextLine());

             System.out.println(new ContaTerminal(numero,agencia,nomeCliente,saldo));
         } catch (InputMismatchException e) {
             System.out.println("Erro: Entrada inválida. Certifique-se de digitar o tipo correto de dado.");
         } finally {
             sc.close();
         }
    }
}