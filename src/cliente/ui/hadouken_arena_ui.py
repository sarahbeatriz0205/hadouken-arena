import tkinter as tk
from tkinter import ttk, messagebox
from zeep import Client

class HadoukenArenaApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Hadouken Arena - Gerenciador de Campeonatos")
        self.root.geometry("800x600")
        self.root.minsize(700, 500)

        self.style = ttk.Style()
        self.style.theme_use("clam")

        self.notebook = ttk.Notebook(root)
        self.notebook.pack(fill="both", expand=True, padx=10, pady=10)

        self.tab_org = ttk.Frame(self.notebook)
        self.notebook.add(self.tab_org, text="Painel do Organizador")
        self.setup_organizador_tab()

        self.tab_esp = ttk.Frame(self.notebook)
        self.notebook.add(self.tab_esp, text="Espectador / Detalhes")
        self.setup_espectador_tab()

        self.atualizar_listas()

    def setup_organizador_tab(self):
        frame_camp = ttk.LabelFrame(self.tab_org, text=" Criar Campeonato ", padding=15)
        frame_camp.pack(fill="x", padx=10, pady=10)

        ttk.Label(frame_camp, text="Nome do Campeonato:").grid(row=0, column=0, sticky="w", pady=5)
        self.nome_camp = ttk.Entry(frame_camp, width=30)
        self.nome_camp.grid(row=0, column=1, padx=5, pady=5)

        ttk.Label(frame_camp, text="Quantidade de jogadores:").grid(row=1, column=0, sticky="w", pady=5)
        self.qtd_jogadores = ttk.Combobox(frame_camp, values=["4", "8"], width=28, state="readonly")
        self.qtd_jogadores.grid(row=1, column=1, padx=5, pady=5)
        self.qtd_jogadores.set("4")

        btn_criar = ttk.Button(frame_camp, text="Criar Campeonato", command=self.acao_criar_campeonato)
        btn_criar.grid(row=2, column=0, columnspan=2, pady=10)

        frame_jog = ttk.LabelFrame(self.tab_org, text=" Cadastrar Jogadores ", padding=15)
        frame_jog.pack(fill="x", padx=10, pady=10)

        ttk.Label(frame_jog, text="Selecione o Campeonato:").grid(row=0, column=0, sticky="w", pady=5)
        self.combo_camps = ttk.Combobox(frame_jog, width=28, state="readonly")
        self.combo_camps.grid(row=0, column=1, padx=5, pady=5)

        ttk.Label(frame_jog, text="Nome do Jogador:").grid(row=1, column=0, sticky="w", pady=5)
        self.nome_jog = ttk.Entry(frame_jog, width=30)
        self.nome_jog.grid(row=1, column=1, padx=5, pady=5)

        btn_cadastrar = ttk.Button(frame_jog, text="Cadastrar Jogador", command=self.acao_cadastrar_jogador)
        btn_cadastrar.grid(row=2, column=0, columnspan=2, pady=10)

    def setup_espectador_tab(self):
        frame_lista = ttk.LabelFrame(self.tab_esp, text=" Campeonatos Existentes (Clique duas vezes para detalhar) ", padding=15)
        frame_lista.pack(fill="both", expand=True, padx=10, pady=10)

        self.tree_camps = ttk.Treeview(frame_lista, columns=("ID", "Nome"), show="headings", height=6)
        self.tree_camps.heading("ID", text="ID")
        self.tree_camps.heading("Nome", text="Nome do Campeonato")

        self.tree_camps.column("ID", width=60, anchor="center")
        self.tree_camps.column("Nome", width=400, anchor="w")
        self.tree_camps.pack(fill="both", expand=True, pady=5)

        self.tree_camps.bind("<Double-1>", self.acao_ver_detalhes)

        btn_detalhes = ttk.Button(frame_lista, text="Ver Detalhes do Campeonato", command=self.acao_ver_detalhes)
        btn_detalhes.pack(pady=5)

        frame_chave = ttk.LabelFrame(self.tab_esp, text=" Chave do Torneio ", padding=15)
        frame_chave.pack(fill="both", expand=True, padx=10, pady=10)

        txt_chave_exemplo = (
            " [ Semifinal 1 ]          [ Final ]\n"
            "  Ryu   [ V ]\n"
            "  E.Honda [ 0 ]   ------>   Ryu\n"
            "                            vs      ------> [ CAMPEÃO ]\n"
            "  Ken   [ V ]               Guile\n"
            "  Chun-Li [ 0 ]   ---------\n"
            " [ Semifinal 2 ]"
        )

        lbl_chave = tk.Label(frame_chave, text=txt_chave_exemplo, font=("Courier", 10), justify="left", bg="#f0f0f0")
        lbl_chave.pack(fill="both", expand=True)

    def atualizar_listas(self):
        try:
            wsdl_campeonato = "http://127.0.0.1:9876/campeonato?wsdl"
            cliente_campeonato = Client(wsdl=wsdl_campeonato)

            for item in self.tree_camps.get_children():
                self.tree_camps.delete(item)

            resposta = cliente_campeonato.service.listarCampeonatos()

            if resposta:
                lista = [c.strip() for c in resposta.split(",") if c.strip()]
                self.combo_camps['values'] = lista
                if lista:
                    self.combo_camps.set(lista[0])

                for nome_camp in lista:
                    try:
                        cid = cliente_campeonato.service.getCampeonatoPorNome(nome_camp)
                        self.tree_camps.insert("", "end", values=(cid, nome_camp))
                    except:
                        pass
            else:
                self.combo_camps['values'] = ["Nenhum campeonato encontrado"]
                self.combo_camps.set("Nenhum campeonato encontrado")
        except Exception as e:
            pass

    def acao_criar_campeonato(self):
        nome = self.nome_camp.get().strip()
        qtd = self.qtd_jogadores.get().strip()

        if not nome:
            messagebox.showerror("Erro", "Digite o nome do campeonato!")
            return

        try:
            wsdl_campeonato = "http://127.0.0.1:9876/campeonato?wsdl"
            cliente_campeonato = Client(wsdl=wsdl_campeonato)

            res = cliente_campeonato.service.criarCampeonato(nome, int(qtd))
            messagebox.showinfo("Sucesso", f"Campeonato '{nome}' criado com sucesso!")

            self.nome_camp.delete(0, tk.END)
            self.atualizar_listas()
            return res
        except Exception as e:
            messagebox.showerror("Erro no Servidor", f"Deu ruim:\n{e}")

    def acao_cadastrar_jogador(self):
        nome_jogador = self.nome_jog.get().strip()
        camp_selecionado = self.combo_camps.get().strip()

        if not nome_jogador:
            messagebox.showerror("Erro", "Digite o nome do jogador!")
            return
        if not camp_selecionado or "Nenhum" in camp_selecionado:
            messagebox.showerror("Erro", "Selecione um campeonato válido!")
            return

        try:
            wsdl_campeonato = "http://127.0.0.1:9876/campeonato?wsdl"
            cliente_campeonato = Client(wsdl=wsdl_campeonato)

            campeonato_id = cliente_campeonato.service.getCampeonatoPorNome(camp_selecionado)
            res = cliente_campeonato.service.inscreverJogador(nome_jogador, campeonato_id)

            messagebox.showinfo("Sucesso", f"Jogador '{nome_jogador}' inscrito!")
            self.nome_jog.delete(0, tk.END)
            return res
        except Exception as e:
            messagebox.showerror("Erro", f"Erro ao inscrever jogador:\n{e}")

    def acao_ver_detalhes(self, event=None):
        selecao = self.tree_camps.selection()
        if not selecao:
            messagebox.showwarning("Aviso", "Selecione um campeonato na tabela para ver os detalhes!")
            return

        item = self.tree_camps.item(selecao)
        campeonato_id = item['values'][0]
        nome_camp = item['values'][1]

        try:
            wsdl_campeonato = "http://127.0.0.1:9876/campeonato?wsdl"
            cliente_campeonato = Client(wsdl=wsdl_campeonato)

            detalhes = cliente_campeonato.service.detalharCampeonato(campeonato_id)

            top = tk.Toplevel(self.root)
            top.title(f"Detalhes - {nome_camp}")
            top.geometry("400x250")

            lbl_titulo = tk.Label(top, text=f"Informações do Torneio", font=("Arial", 12, "bold"))
            lbl_titulo.pack(pady=10)

            txt_detalhes = tk.Text(top, height=8, width=45)
            txt_detalhes.pack(padx=20, pady=5)

            txt_detalhes.insert(tk.END, f"{detalhes}\n")
            txt_detalhes.config(state="disabled")

        except Exception as e:
            messagebox.showerror("Erro", f"Não foi possível carregar os detalhes: {e}")

if __name__ == "__main__":
    root = tk.Tk()
    app = HadoukenArenaApp(root)
    root.mainloop()