import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        criarTela();
    }

    static void criarTela() {

        JFrame frame = new JFrame("Resultado das Atividades");
        JTextArea area = new JTextArea();

        // =========================
        // ATV1
        List<Integer> numeros = Arrays.asList(10, 7, 15, 20, 33, 42, 8, 19);
        List<Integer> pares = numeros.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        // =========================
        // ATV2
        List<String> nomes = Arrays.asList("roberto", "josé", "caio", "vinicius");
        List<String> maiusculos = nomes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        // =========================
        // ATV3
        List<String> palavras = Arrays.asList("se", "talvez", "hoje", "sábado", "se", "quarta", "sábado");

        Map<String, Long> contagem = palavras.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        // =========================
        // ATV4 + ATV5
        List<Produto> produtos = Arrays.asList(
                new Produto("Mouse", 80),
                new Produto("Teclado", 150),
                new Produto("Monitor", 900),
                new Produto("Headset", 120)
        );

        List<Produto> filtrados = produtos.stream()
                .filter(p -> p.getPreco() > 100)
                .collect(Collectors.toList());

        double soma = produtos.stream()
                .mapToDouble(Produto::getPreco)
                .sum();

        // =========================
        // ATV6
        List<String> linguagens = Arrays.asList("Java", "Python", "C", "JavaScript", "Ruby");

        List<String> ordenadas = linguagens.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());

        // =========================
        // MONTAR TEXTO DA TELA
        StringBuilder sb = new StringBuilder();

        sb.append("ATV1 - Pares: ").append(pares).append("\n\n");
        sb.append("ATV2 - Maiúsculo: ").append(maiusculos).append("\n\n");

        sb.append("ATV3 - Contagem:\n");
        contagem.forEach((k, v) -> sb.append(k).append(" = ").append(v).append("\n"));
        sb.append("\n");

        sb.append("ATV4 - Produtos > 100: ").append(filtrados).append("\n\n");

        sb.append("ATV5 - Soma: R$ ").append(soma).append("\n\n");

        sb.append("ATV6 - Ordenadas: ").append(ordenadas).append("\n");

        area.setText(sb.toString());
        area.setEditable(false);

        frame.add(new JScrollPane(area));
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Produto {
    private String nome;
    private double preco;

    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public String toString() {
        return nome + " - R$ " + preco;
    }
}