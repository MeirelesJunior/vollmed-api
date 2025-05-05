package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;


//RECEBENDO UM DTO
public record DadosCadastroMedico(
        //Fazendo validações com Bin validation

        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotNull
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{4,6}")  //Expressão regular pra validar o CRM
        String crm,

        @NotNull
        Especialidade especialidade,

        @NotNull @Valid //Quando tem outro DTO precisa colocar o @Valid pra validar
        DadosEndereco endereco) {
}
