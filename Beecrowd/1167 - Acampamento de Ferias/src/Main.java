import java.io.IOException;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws IOException {
    Scanner read = new Scanner(System.in);
    Integer criancas = read.nextInt();
    while (criancas != 0) {
      Circulo circulo = new Circulo();
      for (int i = 0; i < criancas; i++) {
        String nome = read.next();
        Integer valorFicha = read.nextInt();
        circulo.adicionar(new Crianca(nome, new Ficha(valorFicha)));
      }
      System.out.println("Vencedor(a): " + CriancaVencedora.definir(circulo).getNome());
      criancas = read.nextInt();
    }
    read.close();
  }
}

class Ficha {
  private Integer valor;

  public Ficha(Integer valor) {
    this.valor = valor;
  }

  public Integer getValor() {
    return valor;
  }

  public Boolean isPar() {
    return valor % 2 == 0;
  }

  @Override
  public String toString() {
    return "{valor=" + valor + "}";
  }
}

class Crianca {
  private String nome;
  private Ficha ficha;

  public Crianca(String nome, Ficha ficha) {
    this.nome = nome;
    this.ficha = ficha;
  }

  public String getNome() {
    return nome;
  }

  public Ficha getFicha() {
    return ficha;
  }

  @Override
  public String toString() {
    return "{nome=" + nome + ", ficha=" + ficha + "}";
  }
}

class Circulo {
  private Nodo nodoPrimeiraCrianca;
  private Integer tamanho;

  public Circulo() {
    this.nodoPrimeiraCrianca = null;
    this.tamanho = 0;
  }

  public void adicionar(Crianca crianca) {
    if (nodoPrimeiraCrianca == null) {
      nodoPrimeiraCrianca = new Nodo(crianca);
      nodoPrimeiraCrianca.anterior = nodoPrimeiraCrianca;
      nodoPrimeiraCrianca.proximo = nodoPrimeiraCrianca;
    } else {
      Nodo ultimoNodo = nodoPrimeiraCrianca.anterior;
      Nodo novoNodo = new Nodo(ultimoNodo, crianca, nodoPrimeiraCrianca);
      ultimoNodo.proximo = novoNodo;
      nodoPrimeiraCrianca.anterior = novoNodo;
    }
    tamanho++;
  }

  private Nodo buscarNodo(Crianca crianca) {
    if (nodoPrimeiraCrianca.crianca.equals(crianca))
      return nodoPrimeiraCrianca;

    Nodo aux = nodoPrimeiraCrianca.proximo;
    while (!nodoPrimeiraCrianca.equals(aux)) {
      if (aux.crianca.equals(crianca))
        return aux;
      aux = aux.proximo;
    }

    return null;
  }

  public void remover(Crianca crianca) {
    Nodo nodo = buscarNodo(crianca);
    Nodo proximo = nodo.proximo;
    Nodo anterior = nodo.anterior;

    anterior.proximo = nodo.proximo;
    proximo.anterior = nodo.anterior;

    nodo.anterior = null;
    nodo.proximo = null;

    if (nodo.equals(nodoPrimeiraCrianca)) {
      Crianca criancaAtual = nodo.crianca;
      Ficha fichaCriancaAtaul = criancaAtual.getFicha();
      if (fichaCriancaAtaul.isPar()) {
        nodoPrimeiraCrianca = proximo;
      } else {
        nodoPrimeiraCrianca = anterior;
      }
    }

    tamanho--;
  }

  public Circulo andarHorario(Integer criancas) {
    for (int i = 0; i < criancas; i++) {
      nodoPrimeiraCrianca = nodoPrimeiraCrianca.anterior;
    }
    return this;
  }

  public Circulo andarAntiHorario(Integer criancas) {
    for (int i = 0; i < criancas; i++) {
      nodoPrimeiraCrianca = nodoPrimeiraCrianca.proximo;
    }
    return this;
  }

  public void mostrar() {
    System.out.println("Tamanho: " + tamanho);
    StringBuilder builder = new StringBuilder("*" + nodoPrimeiraCrianca.crianca.getNome());

    Nodo aux = nodoPrimeiraCrianca.proximo;
    while (!aux.equals(nodoPrimeiraCrianca)) {
      Crianca crianca = aux.crianca;
      builder.append(" -> " + crianca.getNome());
      aux = aux.proximo;
    }
    builder.append("\n");
    System.out.println(builder);
  }

  public Integer getTamanho() {
    return tamanho;
  }

  public Crianca getPrimeiraCrianca() {
    return nodoPrimeiraCrianca.crianca;
  }

  private static class Nodo {
    Crianca crianca;
    Nodo proximo;
    Nodo anterior;

    Nodo(Crianca crianca) {
      this.crianca = crianca;
      this.proximo = null;
      this.anterior = null;
    }

    Nodo(Nodo anterior, Crianca crianca, Nodo proximo) {
      this.crianca = crianca;
      this.proximo = proximo;
      this.anterior = anterior;
    }
  }
}

class CriancaVencedora {
  public static Crianca definir(Circulo circulo) {
    if (circulo.getTamanho() == 0) {
      System.out.println("Não há crianças no circulo.");
      return null;
    }

    Ficha fichaDaVez = circulo.getPrimeiraCrianca().getFicha();

    while (circulo.getTamanho() > 1) {
    
      if (fichaDaVez.isPar()) {
        circulo.andarHorario(fichaDaVez.getValor());
      } else {
        circulo.andarAntiHorario(fichaDaVez.getValor());
      }

      Crianca eliminada = circulo.getPrimeiraCrianca();

      fichaDaVez = eliminada.getFicha();

      circulo.remover(eliminada);
    }
    
    return circulo.getPrimeiraCrianca();
  }
}