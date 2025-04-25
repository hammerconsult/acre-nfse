package br.com.webpublico.domain.dto;

import br.com.webpublico.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArquivoNfseDTO extends AbstractEntity implements RowMapper<ArquivoNfseDTO>, BatchPreparedStatementSetter {

    private String descricao;
    private String nome;
    private String mimeType;
    private Long tamanho;
    private List<ArquivoParteNfseDTO> partes;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public List<ArquivoParteNfseDTO> getPartes() {
        return partes;
    }

    public void setPartes(List<ArquivoParteNfseDTO> partes) {
        this.partes = partes;
    }

    public ArquivoNfseDTO toArquivo(String conteudo, String nome) throws IOException {
        String dataInfo = conteudo.contains("base64") ? conteudo.split(";base64,")[0] : "data:image/jpeg";
        String data = conteudo.contains("base64") ? conteudo.split("base64,")[1] : conteudo;
        Base64 decoder = new Base64();
        byte[] imgBytes = decoder.decode(data);
        ArquivoNfseDTO arquivo = new ArquivoNfseDTO();
        arquivo.setNome(nome);
        arquivo.setMimeType(dataInfo.split(":")[1]);
        arquivo.setPartes(new ArrayList<>());
        InputStream is = new ByteArrayInputStream(imgBytes);
        int bytesLidos = 0;
        while (true) {
            int restante = is.available();
            byte[] buffer = new byte[restante > ArquivoParteDTO.TAMANHO_MAXIMO ? ArquivoParteDTO.TAMANHO_MAXIMO : restante];
            bytesLidos = is.read(buffer);
            if (bytesLidos <= 0) {
                break;
            }
            ArquivoParteNfseDTO arquivoParte = new ArquivoParteNfseDTO();
            arquivoParte.setDados(buffer);
            arquivo.getPartes().add(arquivoParte);
        }
        arquivo.setTamanho((long) imgBytes.length);
        return arquivo;
    }

    private ByteArrayOutputStream getByteArrayOutputStream() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        List<ArquivoParteNfseDTO> partesOrdenadas = getPartes();
        partesOrdenadas = partesOrdenadas.stream().sorted(Comparator.comparing(ArquivoParteNfseDTO::getId)).collect(Collectors.toList());
        for (ArquivoParteNfseDTO arquivoParte : partesOrdenadas) {
            buffer.write(arquivoParte.getDados());
        }
        return buffer;
    }

    public String montarConteudo() {
        try {
            if (getPartes() != null && !getPartes().isEmpty()) {
                ByteArrayOutputStream buffer = getByteArrayOutputStream();
                Base64 codec = new Base64();
                return "data:" + getMimeType() + ";base64," + new String(codec.encode(buffer.toByteArray()));
            }
        } catch (Exception e) {
        }
        return "";
    }

    @Override
    public ArquivoNfseDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        ArquivoNfseDTO dto = new ArquivoNfseDTO();
        dto.setId(resultSet.getLong("id"));
        dto.setDescricao(resultSet.getString("descricao"));
        dto.setMimeType(resultSet.getString("mimetype"));
        dto.setNome(resultSet.getString("nome"));
        dto.setTamanho(resultSet.getLong("tamanho"));
        return dto;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, descricao);
        preparedStatement.setString(3, mimeType);
        preparedStatement.setString(4, nome);
        preparedStatement.setLong(5, tamanho);
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
