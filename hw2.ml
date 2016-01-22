type ('nonterminal, 'terminal) symbol =
| N of 'nonterminal
| T of 'terminal

let rec process_level rule_func cur_rule acc_func der frag =
 if cur_rule = [] then acc_func der frag else match frag with
  | [] -> None
  | h::t -> match cur_rule with
    | (N a)::tl -> go_down_level a rule_func (rule_func a) (process_level rule_func tl acc_func) der frag
    | (T a)::tl -> if (a = h) then (process_level rule_func tl acc_func der t) else None
and go_down_level cur rule_func cur_rule_list acc_func der frag = 
  match cur_rule_list with
  | [] -> None
  | h::t -> match (process_level rule_func h acc_func (der@[(cur, h)]) frag) with
    | None -> (go_down_level cur rule_func t acc_func der frag)
    | a -> a

let parse_prefix gram accept frag = match gram with
| (cur, rule_func) -> (go_down_level cur rule_func (rule_func cur) accept [] frag);;
