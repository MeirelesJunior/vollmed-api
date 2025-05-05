package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//A anotação @DataJpaTest é utilizada para testar uma interface Repository.
/*Toda vez que fizermos um teste que acessa o banco de dados, temos duas abordagens possíveis para usar.
Por padrão, o Spring não usará o mesmo banco de dados da aplicação, mas um banco de dados in-memory, ou seja,
 um banco embutido (embedded). Este pode ser o H2, o Derby ou qualquer outro banco de dados embutido em memória.
*AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) seleciona o database da aplicação
@ActiveProfiles("test") indica ao spring pra carregar esse properties de test
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    MedicoRepository medicoRepository;

    @Test
    @DisplayName("Devolver null quando unico medico cadastrado não está disponivel na data.")
    void escolherMedicoAleatorioLivreNaDataCenario1() {
        //GIVEN OU ARRANGE
        var proximaSegundaAs10= LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(10).withMinute(0).withSecond(0).withNano(0);


        var medico = cadastrarMedico("Medico","medico@voll.med","123656",Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente","paciente@email.com","00000000000");
        cadastrarConsulta(medico,paciente,proximaSegundaAs10);

        //WHEN OU ACT
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        //THEN OU ASSERT
        assertThat(medicoLivre).isNull();
    }


    //Os métodos abaixo servem para criar os DTOs que representam o médico, o paciente e a consulta, além de métodos
    // para salvá-los no banco de dados usando o Entity Manager.

    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data){
       em.persist(new Consulta(null,medico,paciente,data,null));
    }

    private Medico cadastrarMedico(String nome, String email,String crm, Especialidade especialidade){
        var medico = new Medico(dadosMedico(nome,email,crm,especialidade));
        em.persist(medico);
        return medico;
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf){
        var paciente = new Paciente(dadosPaciente(nome,email,cpf));
        em.persist(paciente);
        return paciente;
    }

    private DadosCadastroMedico dadosMedico(String nome, String email,String crm, Especialidade especialidade){
        return  new DadosCadastroMedico(
                nome,
                email,
                "61999999999",
                crm,
                especialidade,
                dadosEndereco()
        );
    }

    private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf){
        return new DadosCadastroPaciente(
                nome,
                email,
                "61999999999",
                cpf,
                dadosEndereco()
        );
    }

    private DadosEndereco dadosEndereco(){
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}