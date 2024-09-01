;;=============================================================
;; CS 2110 - Spring 2024
;; Homework 5 - Fibonacci
;;=============================================================
;; Name: Jinlin Yang
;;=============================================================


;; Suggested Pseudocode (see PDF for explanation)
;;
;; n = mem[N];
;; resAddr = mem[RESULT]
;; 
;; if (n == 1) {
;;     mem[resAddr] = 0;
;; } else if (n > 1) {
;;     mem[resAddr] = 0;
;;     mem[resAddr + 1] = 1;
;;     for (i = 2; i < n; i++) {
;;         x = mem[resAddr + i - 1];
;;         y = mem[resAddr + i - 2];
;;         mem[resAddr + i] = x + y;
;;     }
;; }

.orig x3000
    ;; YOUR CODE HERE
    ;R1 is n
    LD R1, N ; Initialize R1 with the value of mem[N], R1 stores n
    ;R2 is resAddr
    LD R2, RESULT ; Initialize R2 with the value of mem[RESULT], R2 stores resAddr
    
    ADD R1, R1, #-1 ; check to see if n == 1， n - 1 ?= 0
    BRNP ELSEIF ; if n - 1 > 0, we do else block
    
    ; if it is we do if block:
    AND R1, R1, #0 ; we first create value 0 with n since we do not need n in if block
    STR R1, R2, #0 ; we store 0 into memory address specified by resAddr 
    BR OVER ; after if block program ends, we must use branch because JMP requires a register
    
ELSEIF    
    ; here we have the else block
    ; we wanna make sure that it only execute if n - 1 > 0
    ; if n - 1 < 0, we do nothing and branch to over!
    ADD R1, R1, #0
    BRN OVER
    
    ; need do add 1 back to R1 to ensure that n is the correct value
    ADD R1, R1, #1
    
    ; Here is the step for: mem[resAddr] = 0;
    AND R3, R3, #0 ; here is the value 0
    STR R3, R2, #0 ; store the value of R3 (0) into the memory address specified by R2
    
    ; Here is the step for: mem[resAddr + 1] = 1;
    ADD R2, R2, #1 ;increment resAddr by 1, we have resAddr + 1!
    ADD R3, R3, #1  ;here is the value 1
    STR R3, R2, #0 ; store the value of R3 (1) into the memory address specified by R2 (resAddr + 1)
    ADD R2, R2, #-1 ; restore resAddr + 1 back to resAddr
    
    ; here we have to build a for loop
    ; for (i = 2; i < n; i++) {
    ;   x = mem[resAddr + i - 1];
    ;   y = mem[resAddr + i - 2];
    ;   mem[resAddr + i] = x + y;
    ; }
    
    ; i = 2 
    AND R4, R4, #0
    ADD R4, R4, #2
    
    ; i < n                  =    i - n < 0
        ; let's find -n first!
    NOT R1, R1 ; this is n with all bits flipped
    ADD R1, R1, #1 ; this is -n by 2's complement!
    
FOR ADD R5, R4, R1 ; i - n
    BRzp OVER ; if loop breaks we skip the entire loop body
    
    ;Loop body goes here:
    
    ;we need R0 to hold the value of resAddr + i - 1
    ;Remember that R2 currenly holds resAddr, R4 holds i           /registers R3, R7
    AND R0, R0, #0
    ADD R0, R2, R4
    ADD R0, R0, #-1 ; R0 = resAddr + i - 1
    
    ; we need x = R6, for x = mem[resAddr + i - 1];
    LDR R6, R0, #0
    
    ;we need R0 to hold the value of resAddr + i - 2
    ;Remember that R0 holds resAddr + i - 1, we just need to substract 1 more from it
    ADD R0, R0, #-1 ; R0 = resAddr + i - 2
    
    ; we need y = R7, for y = mem[resAddr + i - 2];
    LDR R7, R0, #0

    ; we need mem[resAddr + i] = x + y;
    ;first restore R0 from resAddr + i - 2 back to resAddr + i
    ADD R0, R0, #2
    ;Now we use R3 again because it was used outside of loop and will never be used again
    ADD R3, R6, R7 ; R3 = x + y
    STR R3, R0, #0
    
    ;i++
    ADD R4, R4, #1
    ;Loop back to the i < n
    BR FOR
    

OVER 
    HALT

;; Do not rename or remove any existing labels
;; You may change the value of N for debugging
N       .fill 0
RESULT  .fill x4000

    .end

.orig x4000
.blkw 100
.end