import tkinter as tk
from tkinter import ttk, messagebox

class HadoukenArenaApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Hadouken Arena - Gerenciador SFII")
        self.root.geometry("700x500")
        self.root.minsize(600, 450)

        # Configuração de Estilo
        self.style = ttk.Style()
        self.style.theme_use("clam")

        # Criação das Abas (Notebook)
        self.notebook = ttk.Notebook(root)
        self.notebook.pack(fill="both", expand=True, padx=10, pady=10)

        # Aba 1: Painel do Organizador
        self.tab_org = ttk.Frame(self.notebook)
        self.notebook.add(self.tab_org, text="🎮 Painel do Organizador")
        self.setup_organizador_tab()

        # Aba 2: Painel do Espectador (Tempo Real)
        self.tab_esp = ttk.Frame(self.notebook)
        self.notebook.add(self.tab_esp, text="📺 Espectador / Arena")
        self.setup_espectador_tab()

    def setup_organizador_tab(self):
        # Frame de Criação de Campeonato
        frame_camp = ttk.LabelFrame(self.tab_org, text=" Criar Campeonato ", padding=15)
        frame_camp.pack(fill="x", padx=10, pady=10)

        ttk.Label(frame_camp, text="Nome do Campeonato:").grid(row=0, column=0, sticky="w", pady=5)
        self.entry_nome_camp = ttk.Entry(frame_camp, width=30)
        self.entry_nome_camp.grid(row=0, column=1, padx=5, pady=5)

        ttk.Label(frame_camp, text="Capacidade:").grid(row=1, column=0, sticky="w", pady=5)
        self.combo_capacidade = ttk.Combobox(frame_camp, values=["4", "8"], width=28, state="readonly")
        self.combo_capacidade.grid(row=1, column=1, padx=5, pady=5)
        self.combo_capacidade.set("4")

        btn_criar = ttk.Button(frame_camp, text="Criar Campeonato (SOAP)", command=self.acao_criar_campeonato)
        btn_criar.grid(row=2, column=0, columnspan=2, pady=10)

        # Frame de Cadastro de Jogadores
        frame_jog = ttk.LabelFrame(self.tab_org, text=" Cadastrar Jogadores ", padding=15)
        frame_jog.pack(fill="x", padx=10, pady=10)

        ttk.Label(frame_jog, text="Nome do Jogador:").grid(row=0, column=0, sticky="w", pady=5)
        self.entry_nome_jog = ttk.Entry(frame_jog, width=30)
        self.entry_nome_jog.grid(row=0, column=1, padx=5, pady=5)

        btn_cadastrar = ttk.Button(frame_jog, text="Cadastrar Jogador", command=self.acao_cadastrar_jogador)
        btn_cadastrar.grid(row=1, column=0, columnspan=2, pady=10)

    def setup_espectador_tab(self):
        # Placar da Luta Atual
        frame_placar = ttk.LabelFrame(self.tab_esp, text=" Partida em Andamento ", padding=15)
        frame_placar.pack(fill="x", padx=10, pady=10)

        # Jogador 1
        lbl_p1 = ttk.Label(frame_placar, text="Ryu (P1)", font=("Arial", 12, "bold"))
        lbl_p1.grid(row=0, column=0, sticky="w")
        self.progress_p1 = ttk.Progressbar(frame_placar, orient="horizontal", length=250, mode="determinate")
        self.progress_p1.grid(row=1, column=0, padx=5, pady=5)
        self.progress_p1["value"] = 100 # Vida cheia estática

        # Timer e VS
        lbl_vs = ttk.Label(frame_placar, text=" VS \n ⏰ 99", font=("Arial", 10, "bold"), justify="center")
        lbl_vs.grid(row=0, column=1, rowspan=2, padx=20)

        # Jogador 2
        lbl_p2 = ttk.Label(frame_p2 := frame_placar, text="Ken (P2)", font=("Arial", 12, "bold"))
        lbl_p2.grid(row=0, column=2, sticky="e")
        self.progress_p2 = ttk.Progressbar(frame_placar, orient="horizontal", length=250, mode="determinate")
        self.progress_p2.grid(row=1, column=2, padx=5, pady=5)
        self.progress_p2["value"] = 80 # Simulando que tomou umas porradas

        # Visualização da Chave (Simulada em Texto/Canvas)
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

    def acao_criar_campeonato(self):
        nome = self.entry_nome_camp.get()
        cap = self.combo_capacidade.get()
        if not nome:
            messagebox.showerror("Erro", "Digite o nome do campeonato!")
            return
        messagebox.showinfo("Sucesso (Simulado)", f"Campeonato '{nome}' criado com capacidade para {cap} jogadores!")

    def acao_cadastrar_jogador(self):
        nome = self.entry_nome_jog.get()
        if not nome:
            messagebox.showerror("Erro", "Digite o nome do jogador!")
            return
        messagebox.showinfo("Sucesso (Simulado)", f"Jogador '{nome}' cadastrado com sucesso!")
        self.entry_nome_jog.delete(0, tk.END)

if __name__ == "__main__":
    root = tk.Tk()
    app = HadoukenArenaApp(root)
    root.mainloop()