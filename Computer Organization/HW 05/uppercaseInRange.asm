;;=============================================================
;; CS 2110 - Spring 2024
;; Homework 5 - uppercaseInRange 
;;=============================================================
;; Name: Jinlin Yang
;;=============================================================

;;  Suggested Pseudocode (see PDF for explanation)
;;  Pseudocode values are based on the labels' default values.
;;
;;  String str = "touppERcase";
;;  int start = mem[START];
;;  int end = mem[END];
;;  int length = mem[LENGTH];
;;  if (end > length) {
;;      end = length;
;;  }
;;
;;  for (int x = start; x < end; x++) {
;;      if (str[x] >= 'a') {
;;          str[x] = str[x] - 32;
;;      }
;;  }


.orig x3000
    ;; YOUR CODE HERE

; Declaring an Initializing variables!
    ;int end = mem[END];
    ; end = R1
    LD R1, END
    
    ;int length = mem[LENGTH];
    ; length = R2
    LD R2, LENGTH




;;if block begin  
    ;;  if (end > length) {
    ;;      end = length;
    ;;  }
        ; end > length => end - length > 0
        ;; find negative length and store to R3!
        NOT R3, R2
        ADD R3, R3, #1
        ;now let's do end - length, and store the result to R4!
        ADD R4, R1, R3
        ; Branch to FOR if we don't need if block
        BRNZ IFOVER
        ; now let's do the final step of if block
        ; end = length;
        ADD R1, R2, #0
        
;; if block over!
;; at this point, R3 and R4 are free!
IFOVER       




;; For begins
;;  for (int x = start; x < end; x++) {
;;      if (str[x] >= 'a') {
;;          str[x] = str[x] - 32;
;;      }
;;  }
;int x = start, x is the address pointer
    LD R3, START
    
    
; x < end => x - end <0
    ; calculate -end first
    NOT R1, R1
    ADD R1, R1, #1
    ; x - end < 0
FOR ADD R0, R3, R1
    BRZP OVER ; if the condition break, end the program
    
; if condition holds
;loop bractket{
;;      if (str[x] >= 'a') {
;;          str[x] = str[x] - 32;
;;      }
    ; we have to make R4 = str[x], R4 is the value at that address
    ; we have to get the index 0 of the string array first by R7
    LD R7, STRING ; this is the address of str[0]
    ADD R7, R7, R3 ; now this is the address of str[x] because 0 + x = x
    LDR R4, R7, #0 ; now we have R4 = str[x], which is the value
    ; now we do str[x] >= 'a' equal to str[x] - 'a' >= 0
    ; determine -a first
    LD R5, ASCII_A
    NOT R5, R5
    ADD R5, R5, #1
    ; now we do str[x] - 'a'
    ; we store the result in R6
    AND R6, R6, #0
    ADD R6, R4, R5
    ; if result >= 0 do the block
    BRN  SKIP ; if result < 0 we skip the current iteration
    ;R6 is free again
    ; if do the if block, no need to restore 'a' at this point due to above
    ; do str[x] = str[x] - 32;
    ; we first need R6 to hold the value of str[x] - 32
    AND R6, R6, #0
    ADD R6, R4, #-16
    ADD R6, R6, #-16
    ; we have to store str[x] - 32 back to memory!
    STR R6, R7, #0
    
SKIP
    ;x++
    ADD R3, R3, #1
    BR FOR
;loop bracket}

OVER
    HALT

;; Do not change these values!
STRING          .fill x5000
ASCII_A         .fill 97

;; You can change these numbers for debugging!
LENGTH          .fill 11
START           .fill 2
END             .fill 9

.end

.orig x5000                    ;;  Don't change the .orig statement
    .stringz "touppERcase"     ;;  You can change this string for debugging!
.end
