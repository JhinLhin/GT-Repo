;;=============================================================
;; CS 2110 - Spring 2024
;; Homework 5 - hexStringToInt
;;=============================================================
;; Name: Jinlin Yang
;;=============================================================

;;  Suggested Pseudocode (see PDF for explanation)
;;  Pseudocode values are based on the labels' default values.
;;
;;  String hexString = "F1ED";
;;  int length = mem[LENGTH];
;;  int value = 0;
;;  int i = 0;
;;  while (i < length) {
;;      int leftShifts = 4;
;;      while (leftShifts > 0) {
;;          value += value;
;;          leftShifts--;
;;      }
;;      if (hexString[i] >= 65) {
;;          value += hexString[i] - 55;
;;      } else {
;;          value += hexString[i] - 48;
;;      }
;;      i++;
;;  }
;;  mem[mem[RESULTADDR]] = value;

.orig x3000
    ;; YOUR CODE HERE
    
    ;String hexString = "F1ED";
    ;address of hexString[0], not value
    LD R0, HEXSTRING
    
    ;int length = mem[LENGTH];
    LD R1, LENGTH
    
    ;int i = 0;
    AND R2, R2, #0
    
    ;int value = 0;
    AND R3, R3, #0
    
    ; find -length first
    NOT R1, R1
    ADD R1, R1, #1
while ;;while (i < length) = i - length < 0
    ADD R4, R2, R1
    BRZP OVER
;{
    ;int leftShifts = 4;
    AND R4, R4, #0
    ADD R4, R4, #4

    while1    
    ;while (leftShifts > 0)
    ADD R4, R4, #0
    BRNZ while1over
    ;{
       ADD R3, R3, R3 ; value += value;
       ADD R4, R4, #-1 ; leftShifts--;
       BR while1
    ;}
    while1over
    
    ; first find the address of hexString[0] + i
    ADD R5, R0, R2
    ; then we determine the value of hexString[i]
    LDR R5, R5, #0
    
    ; if (hexString[i] - 65 >= 0)
        LD R6, SIXTYFIVE
        NOT R6, R6
        ADD R6, R6, #1
        ADD R6, R5, R6
    ;{   
        BRN else
        ; value += hexString[i] - 55;
        LD R6, ASCIICHAR
        NOT R6, R6
        ADD R6, R6, #1 ; R6 = -55
        ADD R6, R5, R6 ; R6 = hexString[i] - 55
        ADD R3, R3, R6 ; value += hexString[i] - 55;
        BR elseover
    ;}
    else
    ; else {
        ; value += hexString[i] - 48;
        LD R6, ASCIIDIG
        NOT R6, R6
        ADD R6, R6, #1 ; R6 = -48
        ADD R6, R5, R6 ; R6 = hexString[i] - 48
        ADD R3, R3, R6 ; value += hexString[i] - 48;
    ;}
    elseover
    ;i++;
    ADD R2, R2, #1
    BR while
OVER   
;}  
; mem[mem[RESULTADDR]] = value;
STI R3, RESULTADDR
    HALT
    
;; Do not change these values!
ASCIIDIG        .fill 48
ASCIICHAR       .fill 55
SIXTYFIVE       .fill 65
HEXSTRING       .fill x5000
LENGTH          .fill 4
RESULTADDR      .fill x4000
.end

.orig x4000                    ;;Don't change the .orig statement
    ANSWER .blkw 1             ;;Do not change this value
.end


.orig x5000                    ;;  Don't change the .orig statement
    .stringz "F1ED"            ;;  You can change this string for debugging! Hex characters will be uppercase.
.end
