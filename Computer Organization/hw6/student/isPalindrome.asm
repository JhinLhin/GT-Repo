;;=============================================================
;;  CS 2110 - Spring 2024
;;  Homework 6 - isPalindrome
;;=============================================================
;;  Name: Jinlin Yang
;;============================================================

;;  In this file, you must implement the 'isPalindrome' subroutine.
 

.orig x3000
    ;; You do not need to write anything here
    LD R6, STACK_PTR

    ;; Pushes arguments (word addr and len)
    ADD R6, R6, -2
    LEA R0, STRING
    LD R1, LENGTH
    STR R0, R6, 0
    STR R1, R6, 1
    JSR IS_PALINDROME
    LDR R0, R6, 0
    ADD R6, R6, 3
    HALT
    STACK_PTR .fill xF000
    LENGTH .fill 5                 ;; Change this to be length of STRING
    STRING .stringz "rotor"	       ;; You can change this string for debugging!


;;  IS_PALINDROME **RECURSIVE** Pseudocode
;;
;;  isPalindrome(word (addr), len) { 
;;      if (len == 0 || len == 1) {
;;          return 1;
;;      } else {
;;          if (word[0] == word[len - 1]) {
;;              return IS_PALINDROME(word + 1, len - 2);
;;          } else { 
;;              return 0;
;;          }
;;      }
;;  }
;;
IS_PALINDROME ;; Do not change this label! Treat this as like the name of the function in a function header
    ;; Code your implementation for the isPalindrome subroutine here!
    ;; NOTE: Your implementation MUST be done recursively
    
    
    ; Stack Buildup Callee: IS_PALINDROME(word, len)
    ADD R6, R6, -4 ; push 4 wds
                  ; set rv later
    STR R7, R6, 2 ; save RA
    STR R5, R6, 1 ; save old FP
                  ; set local var later
    ADD R5, R6, 0 ; FP = SP
    ADD R6, R6, -5 ; push 5 words
    STR R0, R5, -1 ; save SR1
    STR R1, R5, -2 ; save SR2
    STR R2, R5, -3 ; save SR3
    STR R3, R5, -4 ; save SR4
    STR R4, R5, -5 ; save SR5
    
    
    ; Implementation Callee: IS_PALINDROME(word, len)
    ; argument 1 = string 
    ; argument 2 = length
    ;;  isPalindrome(word (addr), len) { 
    ;;  if (len == 0 || len == 1) {
        LDR R0, R5, 5 ; R0 = length
        ADD R0, R0, -1 ; R0 = length - 1
        BRP ifover
            ;return 1;
            AND R0, R0, 0
            ADD R0, R0, 1
            STR R0, R5, 3
            BR programover
        ifover
    ;;      } else {
    ;;          if (word[0] == word[len - 1]) {
                LDR R1, R5, 4 ; R1 has the address of word[0]
                ADD R2, R1, R0 ; R2 has the address of word[len - 1]
                LDR R1, R1, 0 ; R1 has the value of word[0]
                LDR R2, R2, 0 ; R2 has the value of word[len - 1]
                
                NOT R2, R2
                ADD R2, R2, 1 ; -word[len - 1]
                
                ADD R1, R1, R2 ; R1 = word[0] - word[len - 1]
                BRNP else1over
                LDR R1, R5, 4 ; R1 = the address of word[current] passed in as argument1 
                ADD R1, R1, 1 ; R1 = the address of word[current + 1]
                LDR R2, R5, 5 ; R2 = the value of current len
                ADD R2, R2, -2 ; R2 = the value of currnt len - 2
                
                ;return IS_PALINDROME(word + 1, len - 2);
                ;; Caller Pushes arguments (word addr and len)
                ADD R6, R6, -2
                STR R1, R6, 0
                STR R2, R6, 1
                JSR IS_PALINDROME
                ;; Caller after JSR
                LDR R0, R6, 0
                ADD R6, R6, 3
                
                ; now R0 holds the result of IS_PALINDROME(word + 1, len - 2);
                ; we have to return it
                STR R0, R5, 3
                BR programover
    ;;              
        else1over
    ;;          } else { 
    ;;              return 0;
                AND R0, R0, 0
                STR R0, R5, 3
    ;;          }
    ;;      }
    ;;  }
        programover
    
    
    
    ; Stack Teardown Callee: IS_PALINDROME(word, len)
    LDR	R4, R5, -5	; restore R4
	LDR	R3, R5, -4	; restore R3
	LDR	R2, R5, -3	; restore R2
	LDR	R1, R5, -2	; restore R1
	LDR	R0, R5, -1	; restore R0
	ADD	R6, R5, 0  	; pop saved regs,
				; and local vars
	LDR	R7, R5, 2	; R7 = ret addr
	LDR	R5, R5, 1	; FP = Old FP
	ADD	R6, R6, 3 	; pop 3 words
	RET			; Callee is done we can go back to Caller!
.end