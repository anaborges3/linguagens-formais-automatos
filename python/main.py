import json
import os
from collections import deque

# Função para calcular o fecho-ε (epsilon closure) para um conjunto de estados
def epsilon_closure(states, transitions):
    stack = list(states)
    closure = set(states)

    while stack:
        state = stack.pop()
        if state in transitions and "ε" in transitions[state]:
            for next_state in transitions[state]["ε"]:
                if next_state not in closure:
                    closure.add(next_state)
                    stack.append(next_state)

    return closure

# Função que converte o NFA para DFA
def convert_nfa_to_dfa(nfa):
    nfa_transitions = nfa["transition"]
    alphabet = nfa["alpha"]

    # Mapeamento de conjuntos de estados do NFA para novos estados no DFA
    dfa_states_mapping = {}
    dfa_states_list = []
    queue = deque()
    dfa_transitions = {}
    dfa_final_states = []

    # Fecho-ε do estado inicial
    initial_closure = epsilon_closure({nfa["initial_state"]}, nfa_transitions)
    initial_state_name = "A"
    dfa_states_mapping[frozenset(initial_closure)] = initial_state_name
    dfa_states_list.append(initial_state_name)
    queue.append(initial_closure)

    if any(state in nfa["end_state"] for state in initial_closure):
        dfa_final_states.append(initial_state_name)

    state_count = 1

    while queue:
        current_set = queue.popleft()
        current_state_name = dfa_states_mapping[frozenset(current_set)]
        dfa_transitions[current_state_name] = {}

        # Processar transições para cada símbolo do alfabeto
        for symbol in alphabet:
            move_set = set()
            for state in current_set:
                if state in nfa_transitions and str(symbol) in nfa_transitions[state]:
                    move_set.update(nfa_transitions[state][str(symbol)])

            # Fecho-ε dos estados alcançados
            closure = epsilon_closure(move_set, nfa_transitions)

            if frozenset(closure) not in dfa_states_mapping:
                new_state_name = chr(65 + state_count)  # Nomear estados como A, B, C, etc.
                state_count += 1
                dfa_states_mapping[frozenset(closure)] = new_state_name
                dfa_states_list.append(new_state_name)
                queue.append(closure)

                if any(state in nfa["end_state"] for state in closure):
                    dfa_final_states.append(new_state_name)

            # Adicionar transição para o estado atual no DFA
            dfa_transitions[current_state_name][str(symbol)] = dfa_states_mapping[frozenset(closure)]

    # Construir o DFA
    dfa = {
        "alpha": nfa["alpha"],
        "state": dfa_states_list,
        "initial_state": "A",
        "end_state": dfa_final_states,
        "transition": dfa_transitions
    }

    return dfa

# Função principal para ler o NFA, converter para DFA e salvar o resultado
def main():
    # Caminhos dos arquivos
    input_file = "/workspaces/linguagens-formais-automatos/python/input/input.json"
    output_file = "/workspaces/linguagens-formais-automatos/python/output/dfa.json"

    # Ler o NFA do arquivo JSON
    with open(input_file, 'r') as f:
        nfa = json.load(f)

    # Converter NFA para DFA
    dfa = convert_nfa_to_dfa(nfa)

    # Garantir que o diretório de saída existe
    os.makedirs(os.path.dirname(output_file), exist_ok=True)

    # Escrever o DFA para o arquivo JSON
    with open(output_file, 'w') as f:
        json.dump(dfa, f, indent=4)

    print(f"Conversão concluída! DFA salvo em: {output_file}")

if __name__ == "__main__":
    main()
