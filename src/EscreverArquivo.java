import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EscreverArquivo {
    private static void escreverSolucaoEmArquivo(String nomeArquivo, List<Integer> solucao) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            for (Integer cidade : solucao) {
                writer.write(cidade + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
