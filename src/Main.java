import br.com.dio.dao.UserDAO;
import br.com.dio.exception.CustomException;
import br.com.dio.exception.EmptyStorageException;
import br.com.dio.exception.UserNotFoundException;
import br.com.dio.exception.ValidatorException;
import br.com.dio.model.MenuOption;
import br.com.dio.model.UserModel;
import br.com.dio.validator.UserValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static br.com.dio.validator.UserValidator.verifyModel;

public class Main {

    private final static UserDAO dao = new UserDAO();
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {

            System.out.println("Bem vindo ao cadastro de usuários, selecione a operação desejada");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Atualizar");
            System.out.println("3 - Excluir");
            System.out.println("4 - Buscar por identificador");
            System.out.println("5 - Listar");
            System.out.println("6 - Sair");
            var userInput = scanner.nextInt();

            var selectedOption = MenuOption.values()[userInput - 1];
            switch (selectedOption) {
                case SAVE -> {
                    try {
                        var user = dao.save(requestToSave());
                        System.out.printf("Usuário cadastrado %s", user);
                    } catch (CustomException ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                case UPDATE -> {
                    try {
                        var user = dao.update(requestToUpdate());
                        System.out.printf("Usuário atualizado %s", user);
                    } catch (UserNotFoundException | EmptyStorageException ex) {
                        System.out.println(ex.getMessage());
                    } catch (CustomException ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    } finally {
                        System.out.println("=================");
                    }
                }
                case DELETE -> {
                    try {
                        dao.delete(requestId());
                        System.out.println("Usuário excluído %s");
                    } catch (UserNotFoundException | EmptyStorageException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                case FIND_BY_ID -> {
                    try {
                        var id = requestId();
                        var user = dao.findById(id);
                        System.out.printf("Usuário com id %s:", id);
                        System.out.println(user);
                    } catch (UserNotFoundException | EmptyStorageException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                case FIND_ALL -> {
                    var users = dao.findAll();
                    System.out.println("Usuários cadatrados");
                    System.out.println("===================");
                    users.forEach(System.out::println);
                    System.out.println("=========fim========");
                }
                case EXIT -> System.exit(0);
            }
        }
    }

    private static long requestId() {
        System.out.println("Informe o identificador do usuário:");
        return scanner.nextLong();
    }


    private static UserModel requestToSave(){
        System.out.println("Informe o nome do usuário:");
        var name = scanner.next();
        System.out.println("Informe o email do usuário:");
        var email = scanner.next();
        System.out.println("Informe a data de nascimento do usuário(dd/MM/yyyy):");
        var birthdayString = scanner.next();
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var birthday = LocalDate.parse(birthdayString, formatter);
        return validateInputs(0, name, email, birthday);
    }

    private static UserModel validateInputs(final long id, final String name,
                                            final String email, final LocalDate birthday) {
        var user = new UserModel(0, name, email, birthday);
        try {
            verifyModel(user);
            return user;
        } catch (ValidatorException ex) {
           throw new CustomException("O seu usuário contém erros: " + ex.getMessage(), ex);
        }
    }
    private static UserModel requestToUpdate()  {
        System.out.println("Informe o identificador do usuário:");
        var id = scanner.nextLong();
        System.out.println("Informe o nome do usuário:");
        var name = scanner.next();
        System.out.println("Informe o email do usuário:");
        var email = scanner.next();
        System.out.println("Informe a data de nascimento do usuário(dd/MM/yyyy:");
        var birthdayString = scanner.next();
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var birthday = LocalDate.parse(birthdayString, formatter);
        return validateInputs(id, name, email, birthday);
    }
}