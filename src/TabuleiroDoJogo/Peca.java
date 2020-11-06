package TabuleiroDoJogo;

public abstract class Peca {
	protected Posicao posicao;
	private Tabuleiro Tabuleiro;
	
	public Peca(Tabuleiro Tabuleiro) {
		this.Tabuleiro = Tabuleiro;
	}

	protected Tabuleiro getTabuleiro() {
		return Tabuleiro;
	}	
	
	public abstract boolean[][] movimentoPossiveis();
	
	public boolean movimentoPossivel(Posicao posicao) {
		return movimentoPossiveis()[posicao.getLinha()][posicao.getColuna()];
	}
	
	public boolean temAlgumMovimentoPossivel() {
		boolean[][] mat = movimentoPossiveis();
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat.length; j++) {
				if(mat [i][j]) {
					return true;
				}
			}
		}
		return false;
	}
}
