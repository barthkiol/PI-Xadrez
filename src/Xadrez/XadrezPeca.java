package Xadrez;

import TabuleiroDoJogo.Tabuleiro;
import TabuleiroDoJogo.Posicao;
import TabuleiroDoJogo.Peca;

public abstract class XadrezPeca extends Peca {

	private Cor cor;
	private int moveCount;

	public XadrezPeca(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	public int getContMov() {
		return moveCount;
	}
	
	public void aumentaContMov() {
		moveCount++;
	}

	public void diminuiContMov() {
		moveCount--;
	}

	public PosicaoXadrez getPosicaoXadrez() {
		return PosicaoXadrez.fromPosicao(posicao);
	}
	
	protected boolean temPecaOponente(Posicao posicao) {
		XadrezPeca p = (XadrezPeca)getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}
}