package xyz.ConstruTec.app.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.ConstruTec.app.dao.ObraDao;
import xyz.ConstruTec.app.dao.ProdutoDao;
import xyz.ConstruTec.app.model.ChartData;
import xyz.ConstruTec.app.model.ChartData.Dataset;

@Service
public class ChartService {
    
    @Autowired
    private ProdutoDao produtoDao;
    
    @Autowired
    private ObraDao obraDao;
    
    public ChartData getMovimentacaoEstoque() {
        ChartData chartData = new ChartData();
        
        // Últimos 6 meses
        List<String> labels = new ArrayList<>();
        List<Number> entradas = new ArrayList<>();
        List<Number> saidas = new ArrayList<>();
        
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy");
        
        for (int i = 5; i >= 0; i--) {
            LocalDate mes = hoje.minusMonths(i);
            labels.add(mes.format(formatter));
            
            // TODO: Implementar busca real no banco de dados
            entradas.add((int) (Math.random() * 1000));
            saidas.add((int) (Math.random() * 800));
        }
        
        Dataset datasetEntradas = new Dataset();
        datasetEntradas.setLabel("Entradas");
        datasetEntradas.setData(entradas);
        datasetEntradas.setBackgroundColor("rgba(40, 167, 69, 0.2)");
        datasetEntradas.setBorderColor("#28a745");
        
        Dataset datasetSaidas = new Dataset();
        datasetSaidas.setLabel("Saídas");
        datasetSaidas.setData(saidas);
        datasetSaidas.setBackgroundColor("rgba(220, 53, 69, 0.2)");
        datasetSaidas.setBorderColor("#dc3545");
        
        chartData.setLabels(labels);
        chartData.setDatasets(Arrays.asList(datasetEntradas, datasetSaidas));
        
        return chartData;
    }
    
    public ChartData getProgressoObras() {
        ChartData chartData = new ChartData();
        
        // Dados de exemplo
        List<String> labels = Arrays.asList("Planejamento", "Em Andamento", "Atrasada", "Concluída");
        List<Number> data = Arrays.asList(25, 40, 15, 20);
        
        Dataset dataset = new Dataset();
        dataset.setLabel("Obras por Status");
        dataset.setData(data);
        dataset.setBackgroundColor("rgba(0, 123, 255, 0.2)");
        dataset.setBorderColor("#007bff");
        dataset.setFill(true);
        
        chartData.setLabels(labels);
        chartData.setDatasets(Arrays.asList(dataset));
        
        return chartData;
    }
    
    public ChartData getDistribuicaoProdutos() {
        ChartData chartData = new ChartData();
        
        // Dados de exemplo
        List<String> labels = Arrays.asList("Materiais Básicos", "Ferramentas", "Elétricos", "Hidráulicos", "Acabamento");
        List<Number> data = Arrays.asList(35, 15, 20, 15, 15);
        
        Dataset dataset = new Dataset();
        dataset.setLabel("Distribuição de Produtos");
        dataset.setData(data);
        dataset.setBackgroundColor(Arrays.asList(
            "rgba(255, 99, 132, 0.2)",
            "rgba(54, 162, 235, 0.2)",
            "rgba(255, 206, 86, 0.2)",
            "rgba(75, 192, 192, 0.2)",
            "rgba(153, 102, 255, 0.2)"
        ).toString());
        dataset.setBorderColor(Arrays.asList(
            "#ff6384",
            "#36a2eb",
            "#ffce56",
            "#4bc0c0",
            "#9966ff"
        ).toString());
        
        chartData.setLabels(labels);
        chartData.setDatasets(Arrays.asList(dataset));
        
        return chartData;
    }
} 