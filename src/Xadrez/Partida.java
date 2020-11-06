package Xadrez;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Pecas.Bispo;
import Pecas.Cavalo;
import Pecas.Peao;
import Pecas.Rainha;
import Pecas.Rei;
import Pecas.Torre;
import TabuleiroDoJogo.Peca;
import TabuleiroDoJogo.Posicao;
import TabuleiroDoJogo.Tabuleiro;

public class Partida {

	private static int turno;
	private static Cor jogadorAtual;
	private static Tabuleiro tabuleiro;
	private static boolean check;
	private static boolean checkMate;
	private XadrezPeca enPassantVulnerable;
	private XadrezPeca promocao;

	
	private List<Peca> pecasnoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();
	
	public Partida() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.BRANCO;
		setupInicialP();
	}
	
	public static int getTurno() {
		return turno;
	}
	
	public static Cor getJogadorAtual() {
		return jogadorAtual;
	}
	
	public static boolean getCheck() {
		return check;
	}
	
	public static boolean getCheckMate() {
		return checkMate;
	}
	
	public XadrezPeca getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public XadrezPeca getPromocao() {
		return promocao;
	}
	
	public static XadrezPeca[][] getPecas() {
		XadrezPeca[][] mat = new XadrezPeca[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i=0; i<tabuleiro.getLinhas(); i++) {
			for (int j=0; j<tabuleiro.getColunas(); j++) {
				mat[i][j] = (XadrezPeca) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	public boolean[][] movimentoPossivel(PosicaoXadrez posicaoOrigem) {
		Posicao posicao = posicaoOrigem.toPosicao();
		validarPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentoPossiveis();
	}
	
	public XadrezPeca relizaMovimentoXadrez(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoAlvo) {
		Posicao origem = posicaoOrigem.toPosicao();
		Posicao alvo = posicaoAlvo.toPosicao();
		validarPosicaoOrigem(origem);
		validarPosicaoAlvo(origem, alvo);
		Peca pecaCapturada = realizarMovimento(origem, alvo);
		
		if (testCheck(jogadorAtual)) {
			desfazerMovimento(origem, alvo, pecaCapturada);
			throw new XadrezEr("Voce nao pode se colocar em cheque");
		}
		
		XadrezPeca pecaMovida = (XadrezPeca)tabuleiro.peca(alvo);
		
		// promocao
				promocao = null;
				if (pecaMovida instanceof Peao) {
					if ((pecaMovida.getCor() == Cor.BRANCO && alvo.getLinha() == 0) || (pecaMovida.getCor() == Cor.PRETO && alvo.getLinha() == 7)) {
						promocao = (XadrezPeca)tabuleiro.peca(alvo);
						promocao = trocaPecaPromovida("R");
					}
				}
				
		check = (testCheck(oponente(jogadorAtual))) ? true : false;

		if (testCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
			proximoTurno();
		}
		
		//  en passant
				if (pecaMovida instanceof Peao && (alvo.getLinha() == origem.getLinha() - 2 || alvo.getLinha() == origem.getLinha() + 2)) {
					enPassantVulnerable = pecaMovida;
				}
				else {
					enPassantVulnerable = null;
				}
		
		return (XadrezPeca)pecaCapturada;
	}
	
	public XadrezPeca trocaPecaPromovida(String tipo) {
		if (promocao == null) {
			throw new IllegalStateException("Nao tem nenhuma peca para a promocao");
		}
		if (!tipo.equals("B") && !tipo.equals("N") && !tipo.equals("R") & !tipo.equals("Q")) {
			return promocao;
		}

		Posicao pos = promocao.getPosicaoXadrez().toPosicao();
		Peca p = tabuleiro.removePeca(pos);
		pecasnoTabuleiro.remove(p);

		XadrezPeca novaPeca = newPeca(tipo, promocao.getCor());
		tabuleiro.colocarPeca(novaPeca, pos);
		pecasnoTabuleiro.add(novaPeca);

		return novaPeca;
	}

	private XadrezPeca newPeca(String tipo, Cor cor) {
		if (tipo.equals("B")) return new Bispo(tabuleiro, cor);
		if (tipo.equals("C")) return new Cavalo(tabuleiro, cor);
		if (tipo.equals("R")) return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);
	}

	
	private Peca realizarMovimento(Posicao origem, Posicao alvo) {
		XadrezPeca p = (XadrezPeca)tabuleiro.removePeca(origem);
		p.aumentaContMov();
		Peca pecaCapturada = tabuleiro.removePeca(alvo);
		tabuleiro.colocarPeca(p, alvo);
		
		if (pecaCapturada != null) {
			pecasnoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		
		// rook do lado do rei
				if (p instanceof Rei && alvo.getColuna() == origem.getColuna() + 2) {
					Posicao origemRe = new Posicao(origem.getLinha(), origem.getColuna() + 3);
					Posicao alvoRe = new Posicao(origem.getLinha(), origem.getColuna() + 1);
					XadrezPeca torre = (XadrezPeca)tabuleiro.removePeca(origemRe);
					tabuleiro.colocarPeca(torre, alvoRe);
					torre.aumentaContMov();
				}

				// rook do lado da rainha
				if (p instanceof Rei && alvo.getColuna() == origem.getColuna() - 2) {
					Posicao origemRe = new Posicao(origem.getLinha(), origem.getColuna() - 4);
					Posicao alvoRe = new Posicao(origem.getLinha(), origem.getColuna() - 1);
					XadrezPeca torre = (XadrezPeca)tabuleiro.removePeca(origemRe);
					tabuleiro.colocarPeca(torre, alvoRe);
					torre.aumentaContMov();
				}		
				
				//  en passant
				if (p instanceof Peao) {
					if (origem.getColuna() != alvo.getColuna() && pecaCapturada == null) {
						Posicao posicaoPeao;
						if (p.getCor() == Cor.BRANCO) {
							posicaoPeao = new Posicao(alvo.getLinha() + 1, alvo.getColuna());
						}
						else {
							posicaoPeao = new Posicao(alvo.getLinha() - 1, alvo.getColuna());
						}
						pecaCapturada = tabuleiro.removePeca(posicaoPeao);
						pecasCapturadas.add(pecaCapturada);
						pecasnoTabuleiro.remove(pecaCapturada);
					}
				}
		
		return pecaCapturada;
	}
	
	private void desfazerMovimento(Posicao origem, Posicao alvo, Peca pecaCapturada) {
		XadrezPeca p = (XadrezPeca)tabuleiro.removePeca(alvo);
		p.diminuiContMov();
		tabuleiro.colocarPeca(p, origem);
		
		if (pecaCapturada != null) {
			tabuleiro.colocarPeca(pecaCapturada, alvo);
			pecasCapturadas.remove(pecaCapturada);
			pecasnoTabuleiro.add(pecaCapturada);
		}
		
		// #rook lado do rei
				if (p instanceof Rei && alvo.getColuna() == origem.getColuna() + 2) {
					Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
					Posicao alvoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
					XadrezPeca rook = (XadrezPeca)tabuleiro.removePeca(alvoT);
					tabuleiro.colocarPeca(rook, origemT);
					rook.diminuiContMov();
				}

				// #rook lado da rainha
				if (p instanceof Rei && alvo.getColuna() == origem.getColuna() - 2) {
					Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
					Posicao alvoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
					XadrezPeca rook = (XadrezPeca)tabuleiro.removePeca(alvoT);
					tabuleiro.colocarPeca(rook, origemT);
					rook.diminuiContMov();
				}
				
				//  #en passant
				if (p instanceof Peao) {
					if (origem.getColuna() != alvo.getColuna() && pecaCapturada == enPassantVulnerable) {
						XadrezPeca pawn = (XadrezPeca)tabuleiro.removePeca(alvo);
						Posicao pawnPosicao;
						if (p.getCor() == Cor.BRANCO) {
							pawnPosicao = new Posicao(3, alvo.getColuna());
						}
						else {
							pawnPosicao = new Posicao(4, alvo.getColuna());
						}
						tabuleiro.colocarPeca(pawn, pawnPosicao);
					}
				}
	}
	
	private void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.temAPeca(posicao)) {
			throw new XadrezEr("Nao tem peca na origem");
		}
		if (jogadorAtual != ((XadrezPeca)tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezEr("A peca escolhida nao e sua");
		}
		if (!tabuleiro.peca(posicao).temAlgumMovimentoPossivel()) {
			throw new XadrezEr("A peca escolhida nao tem movimento possivel");
		}
	}
	
	private void validarPosicaoAlvo(Posicao origem, Posicao alvo) {
		if (!tabuleiro.peca(origem).movimentoPossivel(alvo)) {
			throw new XadrezEr("A peca escolhida nao pode se mover no destino escolhido");
		}
	}
	
	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}
	
	private Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}
	
	private XadrezPeca rei(Cor cor) {
		List<Peca> list = pecasnoTabuleiro.stream().filter(x -> ((XadrezPeca)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof Rei) {
				return (XadrezPeca)p;
			}
		}
		throw new IllegalStateException("Nao existe rei " + cor + " no tabuleiro ");
	}
	
	private boolean testCheck(Cor cor) {
		Posicao reiPosicao = rei(cor).getPosicaoXadrez().toPosicao();
		List<Peca> pecasOponente = pecasnoTabuleiro.stream().filter(x -> ((XadrezPeca)x).getCor() == oponente(cor)).collect(Collectors.toList());
		for (Peca p : pecasOponente) {
			boolean[][] mat = p.movimentoPossiveis();
			if (mat[reiPosicao.getLinha()][reiPosicao.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Cor cor) {
		if (!testCheck(cor)) {
			return false;
		}
		List<Peca> list = pecasnoTabuleiro.stream().filter(x -> ((XadrezPeca)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.movimentoPossiveis();
			for (int i=0; i<tabuleiro.getLinhas(); i++) {
				for (int j=0; j<tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((XadrezPeca)p).getPosicaoXadrez().toPosicao();
						Posicao alvo = new Posicao(i, j);
						Peca pecaCapturada = realizarMovimento(origem, alvo);
						boolean testCheck = testCheck(cor);
						desfazerMovimento(origem, alvo, pecaCapturada);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}	
	
	private void colocarNovaPeca(char coluna, int linha, XadrezPeca peca) {
		tabuleiro.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasnoTabuleiro.add(peca);
	}
	
	private void setupInicialP() {
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
        colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));

        colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
        colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
        colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
        colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
        colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));
	}
}