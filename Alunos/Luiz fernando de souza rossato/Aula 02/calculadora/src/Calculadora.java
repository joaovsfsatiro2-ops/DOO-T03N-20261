import java.util.Scanner;

public class Calculadora {
    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do{
            System.out.println("-----calculadora------");
            System.out.println("[1]- calcular preço final ");
            System.out.println("[2]- calcular troco");
            System.out.println("[3]- sair");
            System.out.println("escolher uma das opcao:");

            opcao = scanner.nextInt();





    
           
        switch (opcao) {
            case 1:
                 System.out.println("coloque a quantidade da planta:");
                 double quantidade = scanner.nextDouble();
                 System.out.println("coloque o preço unitario da planta: ");
                 double preço = scanner.nextDouble();
                 double total = quantidade * preço;
                 System.out.println("o preço final é: R$" + total);
                     break;

            case 2:
                System.out.println("digite o valor que o cliente deu: ");
                double valor = scanner.nextDouble();
                System.out.println("digite o valor total da compra");
                double valorTotal = scanner.nextDouble();

                double troco = valor - valorTotal;
                System.out.println("troco final é:  R$"+ troco);
                break;
            case 3:
                System.out.println("encerrando o programa");

                
                break;
        
            default:
                System.out.println("opção invalida");
                break;
        }  
    }while (opcao != 3);
    scanner.close();
}
}
