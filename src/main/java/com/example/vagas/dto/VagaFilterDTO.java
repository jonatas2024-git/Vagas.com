package com.example.vagas.dto;

//import org.springframework.format.annotation.DateTimeFormat; --> Linha removida.
import java.util.List;

public class VagaFilterDTO {
    private String query; 
    private String keyword;
    private String cidade;
    private String uf;
    private String tipoContrato;
    private String senioridade;
    private String modeloTrabalho;
    private List<String> skills; 

    
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }
    public String getSenioridade() { return senioridade; }
    public void setSenioridade(String senioridade) { this.senioridade = senioridade; }
    public String getModeloTrabalho() { return modeloTrabalho; }
    public void setModeloTrabalho(String modeloTrabalho) { this.modeloTrabalho = modeloTrabalho; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}
