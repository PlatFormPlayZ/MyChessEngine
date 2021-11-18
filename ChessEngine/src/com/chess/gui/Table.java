package com.chess.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.board.BoardUtils;
import com.chess.engine.classic.board.Move;
import com.chess.engine.classic.board.Tile;
import com.chess.engine.classic.pieces.Piece;
import com.chess.engine.player.MoveTransition;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Table 
{
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private final Board chessBoard;
	
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
	private static String defaultPieceImagesPath = "art/pieces/color/";
	
	private final Color lightTileColor = Color.decode("#FFFACD"); //"FFFFFF" 
	private final Color darkTileColor = Color.decode("#593E1A");  //"000000" 
	
	public Table()
	{
		this.gameFrame = new JFrame("Jchess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.chessBoard = Board.createStandardBoard();
		
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		
		
		this.gameFrame.setVisible(true);
	}
	
	private JMenuBar createTableMenuBar()
	{
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		return tableMenuBar;
	}
	
	private JMenu createFileMenu()
	{
		final JMenu fileMenu = new JMenu("File");
		
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("open up that pgn file!");
			}
		});
		fileMenu.add(openPGN);
		
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		
		return fileMenu;
	}
	
	private class BoardPanel extends JPanel
	{
		final List<TilePanel> boardTiles;
		
		BoardPanel()
		{
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			for(int i = 0; i < BoardUtils.NUM_TILES; i++)
			{
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
	}
	
	private class TilePanel extends JPanel
	{
		private final int tileId;
		
		TilePanel(final BoardPanel boardPanel, final int tileId)
		{
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(chessBoard);
			
			addMouseListener(new MouseListener()
			{
				@Override
				public void mouseClicked(final MouseEvent e)
				{
					if(isRightMouseButton(event))
					{
						if(sourceTile == null)
						{
							//first click
							sourceTile = chessBoard.getTile(tileId);
							humanMovedPiece = sourceTile.getPiece();
							if(humanMovedPiece == null)
							{
								sourceTile = null;
							}
						}
						else
						{
							//second click
							destinationTile = chessBoard.getTile(tileId);
							final Move move = null;
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
							if(transition.getMoveStatus().isDone())
							{
								//chessBoard = chessBoard.currentPlayer().makeMove(move);
							}
						}
					}
					else if(isLeftMouseButton(event))
					{
						
					}
				}
				
				@Override
				public void mousePressed(final MouseEvent e)
				{
					
				}
				
				@Override
				public void mouseReleased(final MouseEvent e)
				{
					
				}
				
				@Override
				public void mouseEntered(final MouseEvent e)
				{
					
				}
				
				@Override
				public void mouseExited(final MouseEvent e)
				{
					
				}
			});
			
			validate();
		}
		
		private void assignTilePieceIcon(final Board board)
		{
			this.removeAll();
			if(board.getTile(this.tileId).isTileOccupied())
			{
				try
				{
					final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + 
																	  board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + 
																	  board.getTile(this.tileId).getPiece().toString() + ".gif"));
					add(new JLabel(new ImageIcon(image)));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			
		}

		private void assignTileColor() 
		{
			if(BoardUtils.EIGHTH_RANK[this.tileId] || BoardUtils.SIXTH_RANK[this.tileId] || BoardUtils.FOURTH_RANK[this.tileId] || BoardUtils.SECOND_RANK[this.tileId])
			{
				setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
			}
			else if(BoardUtils.SEVENTH_RANK[this.tileId] || BoardUtils.FIFTH_RANK[this.tileId] || BoardUtils.THIRD_RANK[this.tileId] || BoardUtils.FIRST_RANK[this.tileId])
			{
				setBackground(this.tileId % 2 == 0 ? darkTileColor : lightTileColor );
			}
		}
	}
	
}



