package med.voll.api.controller;
//Por se tratarde de um controler a anotação pra teste usada é a @SpringBootTest
//Para conseguirmos injetar o MockMvc na classe ConsultaControllerTest, além da notação @SpringBootTest acima da classe, precisamos da notação @AutoConfigureMockMvc.
//Importar o MockMvcRequestBuilders.post para que o post funcione dentro do perform
//O perform obrigatoriamente pede uma Exception
//Em seguinda utilizar o metodo  .andReturn para pegar o retorno, seguido do getResponse() disparando a requisição
/*
 * O  código abaixo é  utilizado para testar o código 400: primeiro, é disparada uma requisição para o
 * endereço "/consultas" via método POST, sem levar nenhum corpo; na sequência, jogamos o resultado (.andReturn())
 * e o getResponse() em uma variável e verificamos se o status do response é 400, pois nesse cenário, esse é o erro que deve acontecer.
 * @WithMockUser serve para indicar ao Spring que deve ser feito o mock de uma pessoa usuária,
 *  considerando para o Spring Security que estamos logados ao executar o teste.
 * */

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureJsonTesters
class ConsultarControllerTest {

    @MockitoBean
    private AgendaDeConsultas agendaDeConsultas;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Devolver codigo http 400 quando informações estão invalidas")

    @WithMockUser
    void agendar_cenario1() throws Exception {

        var response = mvc.perform(post("/consultas"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @WithMockUser
    @DisplayName("Devolver codigo http 200 quando informações estão válidas")
    void agendar_cenario2() throws Exception {

        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;

        var dadosDetalhamento = new DadosDetalhamentoConsulta(null,2l, 5l, data);

        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);

        var response = mvc
                .perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAgendamentoConsultaJson.write(
                new DadosAgendamentoConsulta(2l,5l,data,especialidade)
                        ).getJson())
                        )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write
                (new DadosDetalhamentoConsulta(null,2l,5l,data)).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

}