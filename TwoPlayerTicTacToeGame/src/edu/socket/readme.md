  The strings that are sent in TTTP are:
  <pre>
  Client -> Server           Server -> Client
  ----------------           ----------------
  MOVE n  (0 <= n <= 8)      WELCOME <char>  (char in {X, O})
  QUIT                       VALID_MOVE
  OTHER_PLAYER_MOVED <n>
  VICTORY
  DEFEAT
  TIE
  MESSAGE <text>
  </pre