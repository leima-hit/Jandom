//! @tags fixpoint
//! @citations GopanR06:f1:p3 GopanR07:p4:f1 GulwaniJK09:f10b:p10 GulwaniZ10:f9:p12

model gopan_cav06_03 {

	var x, y;
	states n1, n2, n3, n4, n5, n6, n_x;

	transition t1 := {
		from := n1;
		to := n2;
		guard := x <= 50;
		action := ;
	};

	transition t2 := {
		from := n2;
		to := n4;
		guard := true;
		action := y' = y + 1;
	};

	transition t3 := {
		from := n1;
		to := n3;
		guard := x >= 51;
		action := ;
	};

	transition t4 := {
		from := n3;
		to := n4;
		guard := true;
		action := y' = y - 1;
	};

	transition t5 := {
		from := n4;
		to := n_x;
		guard := y <= -1;
		action := ;
	};

	transition t6 := {
		from := n4;
		to := n5;
		guard := y >= 0;
		action := ;
	};

	transition t7 := {
		from := n5;
		to := n6;
		guard := true;
		action := x' = x + 1;
	};

	transition t8 := {
		from := n6;
		to := n1;
		guard := true;
		action := ;
	};

}

strategy s {

	Region init := {state = n1 && x = 0 && y = 0};

	Region bad := {state = n1 && (y < 0 || y > x || y > -x + 102)};

}

