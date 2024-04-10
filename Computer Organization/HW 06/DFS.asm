;;=============================================================
;;  CS 2110 - Spring 2024
;;  Homework 6 - DFS
;;=============================================================
;;  Name: Jinlin Yang
;;============================================================

;;  In this file, you must implement the 'SET_VISITED', 'IS_VISITED', and 'DFS' subroutines.


.orig x3000
    ;; You do not need to write anything here
    LD R6, STACK_PTR

    ;; Pushes arguments (address of node 1, target node 5)
    ADD R6, R6, -1 
    AND R1, R1, 0 
    ADD R1, R1, 5
    STR R1, R6, 0 ; argument 2 (value 5) passed in
    ADD R6, R6, -1 ; 
    LD R1, STARTING_NODE_ADDRESS
    STR R1, R6, 0 ; argument 1 (the address of starting node) passed in
    JSR DFS 
    LDR R0, R6, 0
    ADD R6, R6, 3
    HALT

    STACK_PTR .fill xF000
    STARTING_NODE_ADDRESS .fill x6110
    VISITED_VECTOR_ADDR .fill x4199 ;; stores the address of the visited vector.

;;  SET_VISITED Pseudocode

;; Parameter: The address of the node
;; Updates the visited vector to mark the given node as visited

;;  SET_VISITED(addr node) {
;;      visited = mem[mem[VISITED_VECTOR_ADDR]];
;;      data = mem[node];
;;      mask = 1;
;;      while (data > 0) {
;;          mask = mask + mask;
;;          data--;
;;      }
;;      mem[mem[VISITED_VECTOR_ADDR]] = (visited | mask); //Hint: Use DeMorgan's Law!
;;  }

SET_VISITED ;; Do not change this label! Treat this as like the name of the function in a function header
;; Code your implementation for the SET_VISITED subroutine here!
    
    ; we have three local variables, so we might to reserve space
    ; buildup and teardown will be changed
        ;R4
        ;R3
        ;R2
        ;R1
        ;R0
        ;val3
        ;val2
;FP->   ;val1
    ;Stack Buildup -- Callee: SET_VISITED(addr node)
    ADD	R6, R6, -4 ; 
			       ; set rv later
	STR	R7, R6, 2  ; save RA
	STR	R5, R6, 1  ; save old FP
		           ; set local var later
	ADD	R5, R6, 0  ; FP = SP
	ADD	R6, R6, -7 ; push 7 words	
	STR	R0, R5, -3 ; save SR1
	STR	R1, R5, -4 ; save SR2
	STR	R2, R5, -5 ; save SR3
	STR	R3, R5, -6 ; save SR4
	STR	R4, R5, -7 ; save SR5
    
    ;local val 1 = visited = R5 0 = address
    ;local val 2 = data = R5 -1 = address
    ;local val 3 = mask = R5 - 2 = address
    ;implementation -- Callee: SET_VISITED(addr node)

;;  SET_VISITED(addr node) {
;;      visited = mem[mem[VISITED_VECTOR_ADDR]];
        LDI R0, VISITED_VECTOR_ADDR ; R0 = visited
        STR R0, R5, 0 ; store it to local 1
;;      data = mem[node];
        LDR R1, R5, 4 ; R1 = data address
        LDR R1, R1, 0 ; R1 = data value
        STR R1, R5, -1 ; store it to local 2
;;      mask = 1;
        AND R2, R2, 0
        ADD R2, R2, 1 ; R2 = mask
        STR R2, R5, -2 ; store it to local 3
        while1
;;      while (data > 0) {
        ADD R1, R1, 0
        BRNZ while1over
;;          mask = mask + mask;
            ADD R2, R2, R2
            STR R2, R5, -2
;;          data--;
            ADD R1, R1, -1
            STR R1, R5, -1
            BR while1
;;      }
        while1over
;;      mem[mem[VISITED_VECTOR_ADDR]] = (visited | mask); //Hint: Use DeMorgan's Law!
        ; a or b = - (-a and -b)
        NOT R0, R0 ; -visited
        NOT R2, R2 ; -mask
        AND R3, R0, R2 ; R3 = (-visited and -mask)
        NOT R3, R3 ; R3 = -(-visited and -mask)
        STI R3, VISITED_VECTOR_ADDR
;;  }
    
    ;Stack Teardown -- Callee: SET_VISITED(addr node)
    LDR	R4, R5, -7	; restore R4
	LDR	R3, R5, -6	; restore R3
	LDR	R2, R5, -5	; restore R2
	LDR	R1, R5, -4	; restore R1
	LDR	R0, R5, -3	; restore R0
	ADD	R6, R5, 0  	; pop saved regs, and local vars
	LDR	R7, R5, 2	; R7 = ret addr
	LDR	R5, R5, 1	; FP = Old FP
	ADD	R6, R6, 3 	; pop 3 words
	RET			    ; SET_VISITED(addr node) is done
	
	
	
;;  IS_VISITED Pseudocode

;; Parameter: The address of the node
;; Returns: 1 if the node has been visited, 0 if it has not been visited

;;  IS_VISITED(addr node) {
;;       visited = mem[mem[VISITED_VECTOR_ADDR]];
;;       data = mem[node];
;;       mask = 1;
;;       while (data > 0) {
;;           mask = mask + mask;
;;           data--;
;;       }
;;       return (visited & mask) != 0;
;;   }

IS_VISITED ;; Do not change this label! Treat this as like the name of the function in a function header
;; Code your implementation for the IS_VISITED subroutine here!
    
    ; we have three local variables, so we might to reserve space
    ; buildup and teardown will be changed
        ;R4
        ;R3
        ;R2
        ;R1
        ;R0
        ;val3
        ;val2
;FP->   ;val1
    
    ;Stack Buildup -- Callee: IS_VISITED(addr node)
    ADD	R6, R6, -4 ; 
			       ; set rv later
	STR	R7, R6, 2  ; save RA
	STR	R5, R6, 1  ; save old FP
		           ; set local var later
	ADD	R5, R6, 0  ; FP = SP
	ADD	R6, R6, -7 ; push 7 words	
	STR	R0, R5, -3 ; save SR1
	STR	R1, R5, -4 ; save SR2
	STR	R2, R5, -5 ; save SR3
	STR	R3, R5, -6 ; save SR4
	STR	R4, R5, -7 ; save SR5
    
    ;local val 1 = visited = R5 0 = address
    ;local val 2 = data = R5 -1 = address
    ;local val 3 = mask = R5 - 2 = address
    ;implementation -- Callee: IS_VISITED(addr node)
    
;;  IS_VISITED(addr node) {
;;      visited = mem[mem[VISITED_VECTOR_ADDR]];
        LDI R0, VISITED_VECTOR_ADDR ; R0 = visited
        STR R0, R5, 0 ; store it to local 1
;;      data = mem[node];
        LDR R1, R5, 4 ; R1 = data address
        LDR R1, R1, 0 ; R1 = data value
        STR R1, R5, -1 ; store it to local 2
;;      mask = 1;
        AND R2, R2, 0
        ADD R2, R2, 1 ; R2 = mask
        STR R2, R5, -2 ; store it to local 3
        while2
;;      while (data > 0) {
        ADD R1, R1, 0
        BRNZ while2over
;;          mask = mask + mask;
            ADD R2, R2, R2
            STR R2, R5, -2
;;          data--;
            ADD R1, R1, -1
            STR R1, R5, -1
            BR while2
;;      }
        while2over
;;      return (visited & mask) != 0;
        AND R0, R0, R2 
        BRZ false 
        
        ; if it is 1 we return 1
        AND R0, R0, 0
        ADD R0, R0, 1
        STR R0, R5, 3
        BR programover
        false ; we return 0
        AND R0, R0, 0 ; R0 = 0
        STR R0, R5, 3
        programover
;;  }
    
    
    ;Stack Teardown -- Callee: IS_VISITED(addr node)
    LDR	R4, R5, -7	; restore R4
	LDR	R3, R5, -6	; restore R3
	LDR	R2, R5, -5	; restore R2
	LDR	R1, R5, -4	; restore R1
	LDR	R0, R5, -3	; restore R0
	ADD	R6, R5, 0  	; pop saved regs, and local vars
	LDR	R7, R5, 2	; R7 = ret addr
	LDR	R5, R5, 1	; FP = Old FP
	ADD	R6, R6, 3 	; pop 3 words
	RET			    ; IS_VISITED(addr node) is done
    
    


;;  DFS Pseudocode (see PDF for explanation and examples)

;; Parameters: The address of the starting (or current) node, the data of the target node
;; Returns: the address of the node (if the node is found), 0 if the node is not found

;;  DFS(addr node, int target) {
;;        SET_VISITED(node)
;;        if (mem[node] == target) {
;;           return node;
;;        }
;;        result = 0;
;;        for (i = node + 1; mem[i] != 0 && result == 0; i++) {
;;            if (! IS_VISITED(mem[i])) {
;;                result = DFS(mem[i], target);
;;            }
;;        }       
;;        return result;
;;  }

DFS ;; Do not change this label! Treat this as like the name of the function in a function header
    ;; Code your implementation for the DFS subroutine here!
    
    ; we only have one local variable, so this should be fine
    ;Stack Buildup DFS(addr node, int target)
    ADD	R6, R6, -4 ; 
			       ; set rv later
	STR	R7, R6, 2  ; save RA
	STR	R5, R6, 1  ; save old FP
		           ; set local var later
	ADD	R5, R6, 0  ; FP = SP
	ADD	R6, R6, -5 ; push 5 words	
	STR	R0, R5, -1 ; save SR1
	STR	R1, R5, -2 ; save SR2
	STR	R2, R5, -3 ; save SR3
	STR	R3, R5, -4 ; save SR4
	STR	R4, R5, -5 ; save SR5

    ;local val 1 = result = R5 0 = address
    ;implementation -- Callee: DFS(addr node, int target)
    
;; DFS(addr node, int target) {

;       push the argument into SET_VISITED as caller
        LDR R0, R5, 4 ; get node from DFS argument 1
        ADD R6, R6, -1 ; move R6 1 up
        STR R0, R6, 0
        ;SET_VISITED(node)
        JSR SET_VISITED
        ;NOTE: There is no and we do not need the return val
        ; just pop everything off
        ADD R6, R6, 2

;;      if (mem[node] == target) {
        LDR R0, R5, 4 ; get node from DFS argument 1, this is the address
        LDR R0, R0, 0 ; this is the value of node = mem[node]
        LDR R1, R5, 5 ; this is the value of target (argument 2) since it was passed as value
        NOT R1, R1
        ADD R1, R1, 1 ; this is -target
        ADD R0, R0, R1 ; R0 = mem[node] - target
        BRNP ifover
;;          return node;
            LDR R0, R5, 4 ;get node from DFS arguement 1, this is the address
            STR R0, R5, 3 ; set Return value as R0
            BR DFSover
;;      }
        ifover
;;      result = 0;
        AND R0, R0, 0 ; R0 = 0
        STR R0, R5, 0 ; result = R0 = 0
;;      for (i = node + 1; mem[i] != 0 && result == 0; i++) {
        LDR R1, R5, 4 ; get node from DFS argument 1, this is the address
        ADD R1, R1, 1 ; R1 = i = node + 1
        FOR 
            LDR R2, R1, 0 ; R2 = mem[i]
            BRZ Forover
            LDR R0, R5, 0 ; R0 = result
            BRNP Forover
            ;for body {
;;              if (! IS_VISITED(mem[i])) {
                ;Push argument mem[i] as caller
                ADD R6, R6, -1
                STR R2, R6, 0
                JSR IS_VISITED
                ;hold the return value and pop RV and Arguments
                LDR R0, R6, 0 ; R0 = return value
                ADD R6, R6, 2 ; pop them off 
                ADD R0, R0, 0 ; we have to add 0 because of popping set condition code
                BRNP skip
                    
                    ;result = DFS(mem[i], target);
                    ;Push in the arguments as caller here again
                    ADD R6, R6, -2
                    STR R2, R6, 0 ; mem[i] pushed in = argument 1
                    LDR R3, R5, 5 ; R3 = target
                    STR R3, R6, 1 ; target pushed in = argument 2
                    JSR DFS
                    LDR R0, R6, 0 ; R0 = return value
                    STR R0, R5, 0 ; result = return value = first local variable
                    ADD R6, R6, 3 ; pop RV and 2 arguments off
;;              }
            ;}
            skip
            ADD R1, R1, 1
            BR FOR
;;      }    
        Forover
;;      return result;
        LDR R0, R5, 0 ; R0 = result
        STR R0, R5, 3 ; set return value to result
;; }
        DFSover
    
    ;Stack Teardown -- Callee: DFS(addr node, int target)
	LDR	R4, R5, -5	; restore R4
	LDR	R3, R5, -4	; restore R3
	LDR	R2, R5, -3	; restore R2
	LDR	R1, R5, -2	; restore R1
	LDR	R0, R5, -1	; restore R0
	ADD	R6, R5, 0  	; pop saved regs, and local vars
	LDR	R7, R5, 2	; R7 = ret addr
	LDR	R5, R5, 1	; FP = Old FP
	ADD	R6, R6, 3 	; pop 3 words
	RET			; DFS() is done!
.end

;; Assuming the graphs starting node (1) is at address x6100, here's how the graph (see below and in the PDF) is represented in memory
;;
;;         0      3
;;          \   / | \
;;            4   1 - 2 
;;             \ /    |
;;              5  -  6
;;

.orig x4199
    .fill 0 ;; visited set will be at address x4199, initialized to 0
.end

.orig x6110         ;; node 1 itself lives here at x6110
    .fill 1         ;; node.data (1)
    .fill x6120     ;; node 2 lives at this address
    .fill x6130     ;; node 3 lives at this address
    .fill x6150     ;; node 5 lives at this address   
    .fill 0
.end

.orig x6120	        ;; node 2 itself lives here at x6120
    .fill 2         ;; node.data (2)
    .fill x6110     ;; node 1 lives at this address
    .fill x6130     ;; node 3 lives at this address
    .fill x6160     ;; node 6 lives at this address
    .fill 0
.end

.orig x6130	        ;; node 3 itself lives here at x6130
    .fill 3         ;; node.data (3)
    .fill x6110     ;; node 1 lives at this address
    .fill x6120     ;; node 2 lives at this address
    .fill x6140     ;; node 4 lives at this address
    .fill 0
.end

.orig x6140	        ;; node 4 itself lives here at x6140
    .fill 4         ;; node.data (4)
    .fill x6100     ;; node 0 lives at this address
    .fill x6130     ;; node 3 lives at this address
    .fill x6150     ;; node 5 lives at this address
    .fill 0
.end

.orig x6100         ;; node 0 itself lives here at x6000
    .fill 0         ;; node.data (0)
    .fill x6140     ;; node 4 lives at this address
    .fill 0
.end

.orig x6150	        ;; node 5 itself lives here at x6150
    .fill 5         ;; node.data (5)
    .fill x6110     ;; node 1 lives at this address
    .fill x6140     ;; node 4 lives at this address
    .fill x6160     ;; node 6 lives at this address
    .fill 0
.end

.orig x6160	        ;; node 6 itself lives here at x6160
    .fill 6         ;; node.data (6)
    .fill x6120     ;; node 2 lives at this address
    .fill x6150     ;; node 5 lives at this address
    .fill 0
.end
 


