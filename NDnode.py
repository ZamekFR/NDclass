import tkinter as tk
from tkinter import messagebox
import time

class NDNodeApp:
    def __init__(self, root):
        self.root = root
        self.root.title("ND Node")
        self.root.geometry("400x300")

        # Zmienna do paska ładowania
        self.progress = tk.DoubleVar()

        # Tytuł aplikacji
        self.label_title = tk.Label(root, text="ND node", font=("Arial", 24, "bold"), fg="red")
        self.label_title.pack(pady=10)

        # Pasek ładowania
        self.label_loading = tk.Label(root, text="Launching...", font=("Arial", 12))
        self.label_loading.pack(pady=10)

        self.progress_bar = tk.Progressbar(root, variable=self.progress, maximum=100, length=300)
        self.progress_bar.pack(pady=10)

        # Przyciski
        self.button_file = tk.Button(root, text="File", command=self.on_file)
        self.button_file.pack(side="left", padx=10)

        self.button_edit = tk.Button(root, text="Edit", command=self.on_edit)
        self.button_edit.pack(side="left", padx=10)

        self.button_setup = tk.Button(root, text="Setup", command=self.on_setup)
        self.button_setup.pack(side="left", padx=10)

        self.button_preferences = tk.Button(root, text="Preferences", command=self.on_preferences)
        self.button_preferences.pack(side="left", padx=10)

        self.launch()

    def launch(self):
        # Animacja paska ładowania
        for i in range(101):
            self.progress.set(i)
            self.root.update_idletasks()
            time.sleep(0.05)  # Opóźnienie, aby animacja była widoczna
        self.label_loading.config(text="Launch Complete")

    def on_file(self):
        messagebox.showinfo("File", "File menu clicked!")

    def on_edit(self):
        messagebox.showinfo("Edit", "Edit menu clicked!")

    def on_setup(self):
        messagebox.showinfo("Setup", "Setup menu clicked!")

    def on_preferences(self):
        messagebox.showinfo("Preferences", "Preferences menu clicked!")

if __name__ == "__main__":
    root = tk.Tk()
    app = NDNodeApp(root)
    root.mainloop()
