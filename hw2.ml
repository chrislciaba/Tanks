type ('nonterminal, 'terminal) symbol =
| N of 'nonterminal
| T of 'terminal

let rec return_list list x = match list with
| [] -> []
| h::t -> match h with
  | (lhs, rhs) -> if x = lhs then ([rhs]@(return_list t x)) else (return_list t x)

let convert_grammar gram1 = match gram1 with
| (lhs, rhs) -> (lhs, (fun x -> return_list rhs x))

(* process_level: this function works by going through all of
the items at the current level, i.e. the array containing the
rule that we are examining *)
let rec process_level rule_func cur_rule acc_func der frag =

 (* if the current rule is empty, then we are out of options
and need to see if the acceptor will accept this derivation *)

 if cur_rule = [] then acc_func der frag else match frag with

 (* if the fragment contains nothing, then we will not be able
    to find a matching derivation. *)

  | [] -> None
  | h::t -> match cur_rule with

(* if the rule is nonterminal, then we need to go down a level
in the tree in order to try to find a matching terminal symbol.
The most important part of this process is updating the acceptor
function. If the current path returns None and we use the given
acceptor, then we will just get None and that will be it. If
we update the acceptor so that it checks the other rules in the
list as well, then we will be able to check all of the rules
instead of just the first one if it's nonterminal. If it's a
terminal symbol and it's equal to the head of the fragment, then
process the rest of the rule on the current level. Otherwise,
this path doesn't work, so return None and the next function
will handle it. *)

    | (N a)::tl -> go_down_level a rule_func (rule_func a) (process_level rule_func tl acc_func) der frag
    | (T a)::tl -> if (a = h) then (process_level rule_func tl acc_func der t) else None

(* This function handles the list of rules that the function from
the grammar returns. If there are no rules in the list, we cannot
find a derivation, so return None. If there are rules in the list,
then try to see what happens if we use the current rule in the
derivation. If it works, return the derivation. If not, try
the current function with that rule removed so we can see if the
next one will work. *)

and go_down_level cur rule_func cur_rule_list acc_func der frag = 
  match cur_rule_list with
  | [] -> None
  | h::t -> match (process_level rule_func h acc_func (der@[(cur, h)]) frag) with
    | None -> (go_down_level cur rule_func t acc_func der frag)
    | a -> a

let parse_prefix gram accept frag = match gram with
| (cur, rule_func) -> (go_down_level cur rule_func (rule_func cur) accept [] frag);;
