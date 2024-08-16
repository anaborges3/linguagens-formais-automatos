package br.edu.ifgoiano;

import java.util.*;

public class ConjuntoOperacoes {

    public static void main(String[] args) {
        Set<Integer> A = new HashSet<>(Arrays.asList(2, 4));
        Set<Integer> B = new HashSet<>(Arrays.asList(4, 5, 6));
        Set<Integer> U = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        // União
        Set<Integer> uniao = new HashSet<>(A);
        uniao.addAll(B);
        System.out.println("União: " + uniao);

        // Interseção
        Set<Integer> intersecao = new HashSet<>(A); //intersecao
        intersecao.retainAll(B);
        System.out.println("Interseção: " + intersecao);

        // Diferença A - B
        Set<Integer> diferenca = new HashSet<>(A); //diferenca
        diferenca.removeAll(B);
        System.out.println("Diferença (A - B): " + diferenca);

        // Complemento de A em relação a U
        Set<Integer> complemento = new HashSet<>(U); //complemento
        complemento.removeAll(A);
        System.out.println("Complemento de A em U: " + complemento);

        // Conjunto das Partes de A
        Set<Set<Integer>> conjuntoPartes = conjuntoDasPartes(A); //conjunto partes
        System.out.println("Conjunto das Partes de A: " + conjuntoPartes);

        // Produto Cartesiano A x B
        Set<List<Integer>> produtoCartesiano = produtoCartesiano(A, B); //produto cartesiano
        System.out.println("Produto Cartesiano A x B: " + produtoCartesiano);
    }

    public static Set<Set<Integer>> conjuntoDasPartes(Set<Integer> conjunto) {
        Set<Set<Integer>> partes = new HashSet<>();
        if (conjunto.isEmpty()) {
            partes.add(new HashSet<>());
            return partes;
        }

        List<Integer> list = new ArrayList<>(conjunto);
        Integer head = list.get(0);
        Set<Integer> rest = new HashSet<>(list.subList(1, list.size()));

        for (Set<Integer> subset : conjuntoDasPartes(rest)) {
            Set<Integer> newSubset = new HashSet<>();
            newSubset.add(head);
            newSubset.addAll(subset);

            partes.add(subset);
            partes.add(newSubset);
        }

        return partes;
    }

    public static Set<List<Integer>> produtoCartesiano(Set<Integer> A, Set<Integer> B) {
        Set<List<Integer>> produto = new HashSet<>();
        for (Integer a : A) {
            for (Integer b : B) {
                produto.add(Arrays.asList(a, b));
            }
        }
        return produto;
    }
}