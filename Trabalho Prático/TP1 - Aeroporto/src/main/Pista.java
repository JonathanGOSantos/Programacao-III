import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class Pista extends Simulador {
  private Aeroporto aeroporto;
  private Queue<Aviao> decolar;
  private Queue<Aviao> aterrissar1;
  private Queue<Aviao> aterrissar2;

  private Optional<Aviao> aviaoEmEmergência;

  private Boolean permiteAterrisagem;

  public Pista(Aeroporto aeroporto, Boolean permiteAterrisagem) {
    this.aeroporto = aeroporto;
    this.permiteAterrisagem = permiteAterrisagem;

    decolar = new LinkedList<>();
    aterrissar1 = new LinkedList<>();
    aterrissar2 = new LinkedList<>();
  }

  public void adicionarAviaoParaAterrissar(Aviao aviao) {
    if (!permiteAterrisagem) {
      throw new RuntimeException("A pista não permite aterrisagem.");
    }

    if (aterrissar1.size() <= aterrissar2.size()) {
      aterrissar1.add(aviao);
    } else {
      aterrissar2.add(aviao);
    }
  }

  public void adicionarAviaoParaDecolar(Aviao aviao) {
    decolar.add(aviao);
  }

  public void adicionarAviaoEmEmergencia(Aviao aviao) {
    aviaoEmEmergência = Optional.of(aviao);
  }

  public Integer avioesParaAterrissar() {
    return aterrissar1.size() + aterrissar2.size();
  }

  public Integer avioesParaDecolar() {
    return decolar.size();
  }

  public Aviao pegarAviaoMaisAntigo() {
    Queue<Aviao> aterrissar;
    if (aterrissar1.size() <= aterrissar2.size()) {
      aterrissar = aterrissar1;
    } else {
      aterrissar = aterrissar2;
    }

    if (aterrissar.peek().getId() < decolar.peek().getId()) {
      return aterrissar.poll();
    } else {
      return decolar.poll();
    }
  }

  @Override
  public void runTick() {
    if (aviaoEmEmergência.isPresent()) {
      aeroporto.emergencia(aviaoEmEmergência.get());
      aviaoEmEmergência = Optional.empty();
    } else { 
      aeroporto.processar(pegarAviaoMaisAntigo());
    }

    decolar.forEach(Aviao::runTick);
    aterrissar1.forEach(Aviao::runTick);
    aterrissar2.forEach(Aviao::runTick);
  }
}