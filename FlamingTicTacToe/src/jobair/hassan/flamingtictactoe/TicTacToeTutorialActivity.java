package jobair.hassan.flamingtictactoe;



import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import jobair.hassan.flamingtictactoe.R;

 
 
public class TicTacToeTutorialActivity extends Activity {
 
	// Represents the internal state of the game
	private TicTacToeGame mGame;
 
	// Buttons making up the board
	private Button mBoardButtons[];
 
	// Various text displayed
	private TextView mInfoTextView;
	private TextView mPlayerOneCount; 
	private TextView mTieCount;
	private TextView mPlayerTwoCount; 
	private TextView mPlayerOneText; 
	private TextView mPlayerTwoText; 
 
	// Counters for the wins and ties
	private int mPlayerOneCounter = 0; 
	private int mTieCounter = 0;
	private int mPlayerTwoCounter = 0; 
 
	// Booleans to check
	// if game is to be single or multiplayer
	// if it is player one's turn
	// and if the game is over
	private boolean mPlayerOneFirst = true; 
	private boolean mIsSinglePlayer = false; 
	private boolean mIsPlayerOneTurn = true; 
	private boolean mGameOver = false;
	
	//Sounds for the game
	MediaPlayer selected;
	MediaPlayer playerOneWin;
	MediaPlayer playerTwoWin;
	MediaPlayer tie;
	
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	selected = MediaPlayer.create(this, R.raw.select);
    	playerOneWin = MediaPlayer.create(this, R.raw.player_1_win);
    	playerTwoWin = MediaPlayer.create(this, R.raw.player_2_win);
    	tie = MediaPlayer.create(this, R.raw.tie_sound);
    
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.main);
 
        boolean mGameType = getIntent().getExtras().getBoolean("gameType");
 
        // Initialize the buttons
        mBoardButtons = new Button[mGame.getBOARD_SIZE()];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);
 
        // setup the textviews
        mInfoTextView = (TextView) findViewById(R.id.information);
        mPlayerOneCount = (TextView) findViewById(R.id.humanCount); 
        mPlayerOneCount.setTextColor(Color.YELLOW);
        mTieCount = (TextView) findViewById(R.id.tiesCount);
        mTieCount.setTextColor(Color.YELLOW);
        mPlayerTwoCount = (TextView) findViewById(R.id.androidCount); 
        mPlayerTwoCount.setTextColor(Color.YELLOW);
        mPlayerOneText = (TextView) findViewById(R.id.human); 
        mPlayerTwoText = (TextView) findViewById(R.id.android); 
 
        // set the initial counter display values
        mPlayerOneCount.setText(Integer.toString(mPlayerOneCounter));
        mTieCount.setText(Integer.toString(mTieCounter));
        mPlayerTwoCount.setText(Integer.toString(mPlayerTwoCounter));
 
        // create a new game object
        mGame = new TicTacToeGame();
 
        // start a new game
        startNewGame(mGameType);
 
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.game_menu, menu);
 
    	return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case R.id.newGame:
    		startNewGame(mIsSinglePlayer);
    		break;
    	case R.id.exitGame:
    		TicTacToeTutorialActivity.this.finish();
    		break;
    	}
 
    	return true;
    }
 
    // start a new game
    // clears the board and resets all buttons / text
    // sets game over to be false
    private void startNewGame(boolean isSingle)
    {
 
    	this.mIsSinglePlayer = isSingle;
 
    	mGame.clearBoard();
 
    	for (int i = 0; i < mBoardButtons.length; i++)
    	{
    		mBoardButtons[i].setText("");
    		mBoardButtons[i].setEnabled(true);
    		mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
    		mBoardButtons[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.blank));
    	}
 
    	if (mIsSinglePlayer) 
    	{
    		mPlayerOneText.setText("Human:"); 
    		mPlayerTwoText.setText("Android:"); 
 
    		if (mPlayerOneFirst) 
        	{
        		mInfoTextView.setText(R.string.first_human);
        		mPlayerOneFirst = false;
        	}
        	else
        	{
        		mInfoTextView.setText(R.string.turn_computer);
        		int move = mGame.getComputerMove();
        		setMove(mGame.PLAYER_TWO, move);
        		mPlayerOneFirst = true;
        	}
    	}
    	else 
    	{
    		mPlayerOneText.setText("Player One:"); 
    		mPlayerTwoText.setText("Player Two:"); 
 
    		if (mPlayerOneFirst) 
        	{
        		mInfoTextView.setText("Player One's turn"); 
        		mPlayerOneFirst = false;
        	}
        	else
        	{
        		mInfoTextView.setText("Player two's turn"); 
        		mPlayerOneFirst = true;
        	}
    	}
 
    	mGameOver = false;
    }
 
    private class ButtonClickListener implements View.OnClickListener
    {
    	int location;
 
    	public ButtonClickListener(int location)
    	{
    		this.location = location;
    	}
 
    	public void onClick(View view)
    	{
    		if (!mGameOver)
    		{
    			if (mBoardButtons[location].isEnabled())
    			{
    				if (mIsSinglePlayer) 
    				{
    					setMove(mGame.PLAYER_ONE, location);
 
	    				int winner = mGame.checkForWinner();
 
	    				if (winner == 0)
	    				{
	    					mInfoTextView.setText(R.string.turn_computer);
	    					int move = mGame.getComputerMove();
	    					setMove(mGame.PLAYER_TWO, move);
	    					winner = mGame.checkForWinner();    					
	    				}
 
	    				if (winner == 0)
	    					mInfoTextView.setText(R.string.turn_human);
	    				else if (winner == 1)
	    				{	
	    					tie.start();
	    					mInfoTextView.setText(R.string.result_tie);
	    					mTieCounter++;
	    					mTieCount.setText(Integer.toString(mTieCounter));
	    					mGameOver = true;
	    				}
	    				else if (winner == 2)
	    				{	playerOneWin.start();
	    					mInfoTextView.setText(R.string.result_human_wins);
	    					mPlayerOneCounter++;
	    					mPlayerOneCount.setText(Integer.toString(mPlayerOneCounter));
	    					mGameOver = true;
	    				}
	    				else
	    				{	playerTwoWin.start();
	    					mInfoTextView.setText(R.string.result_android_wins);
	    					mPlayerTwoCounter++;
	    					mPlayerTwoCount.setText(Integer.toString(mPlayerTwoCounter));
	    					mGameOver = true;
	    				}
    				}
    				else
    				{
    					if (mIsPlayerOneTurn)
    						setMove(mGame.PLAYER_ONE, location);
    					else
    						setMove(mGame.PLAYER_TWO, location);
 
	    				int winner = mGame.checkForWinner();
 
	    				if (winner == 0)
	    				{
	    					if (mIsPlayerOneTurn)
	    					{
	    						mInfoTextView.setText("Player Two's turn");
	    						mIsPlayerOneTurn = false;
	    					}    					
	    					else
	    					{
	    						mInfoTextView.setText("Player One's turn");
	    						mIsPlayerOneTurn = true;
	    					}
	    				}
	    				else if (winner == 1)
	    				{
	    					tie.start();
	    					mInfoTextView.setText(R.string.result_tie);
	    					mTieCounter++;
	    					mTieCount.setText(Integer.toString(mTieCounter));
	    					mGameOver = true;
	    				}
	    				else if (winner == 2)
	    				{	playerOneWin.start();
	    					mInfoTextView.setText(R.string.result_player_one_wins); 
	    					mPlayerOneCounter++;
	    					mPlayerOneCount.setText(Integer.toString(mPlayerOneCounter));
	    					mGameOver = true;
	    					mIsPlayerOneTurn = false;
	    				}
	    				else
	    				{
	    					playerTwoWin.start();
	    					mInfoTextView.setText(R.string.result_player_two_wins); 
	    					mPlayerTwoCounter++;
	    					mPlayerTwoCount.setText(Integer.toString(mPlayerTwoCounter));
	    					mGameOver = true;
	    					mIsPlayerOneTurn = true;
	    				}
    				}
    			}
    		}
    	}
    }
 
    // set move for the current player
    @SuppressWarnings("deprecation")
	private void setMove(char player, int location)
    {
    	mGame.setMove(player, location);
    	mBoardButtons[location].setEnabled(false);
    	if (player == mGame.PLAYER_ONE)
    		{	mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.x));
    		selected.start();
    		selected.isLooping();
    		}
    	else
    		{mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.o));
    		selected.start();
    		selected.isLooping();
    		}
    	}
}