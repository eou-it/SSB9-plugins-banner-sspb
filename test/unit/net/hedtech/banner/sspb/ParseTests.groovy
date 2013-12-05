package net.hedtech.banner.sspb

class ParseTests extends GroovyTestCase  {
    def cs = new CompileService()

    //Expressions to test old parseExpression functions
    def expressions = [
            [in:"\$var.\$property"  , out:"\$scope.var_property"         , outv:"var_property" ],
            [in:"\$\$var.\$property", out:"\$scope._var.\$.property"     , outv:"_var.\$property"],
            [in:"var.property"      , out:"var.property"                 , outv:"var.property" ],
            [in:"\$var.property"    , out:"\$scope.var.property"         , outv:"var.property" ],
            [in:"\$var.\$visible"   , out:"\$scope.var_visible"          , outv:"var_visible"  ],
            [in:"\$var.\$style"     , out:"\$scope.var_style"            , outv:"var_style"    ],

            [where:"onLoad",in:"if (\$EmployeeData.JOB =='MANAGER') \$\$activateFlow('managerFlow'); else \$\$activateFlow('employeeFlow');",
                    out:"if (\$scope.EmployeeData.JOB =='MANAGER') \$scope._activateFlow('managerFlow'); else \$scope._activateFlow('employeeFlow');",
                    outv:"if (EmployeeData.JOB =='MANAGER') _activateFlow('managerFlow'); else _activateFlow('employeeFlow');"
            ],

            [where:"onUpdate",in:"\$EmployeeData.\$load();",
                    out:"\$scope.EmployeeData_load();",
                    outv:"EmployeeData_load();"
            ],
            [where:"onClick",in:"\$SearchTodo.\$visible=\$showSearchTodo; \$EditTodoList.\$visible = \$showEditTodoList;",
                    out:"\$scope.SearchTodo_visible=\$scope.showSearchTodo; \$scope.EditTodoList_visible = \$scope.showEditTodoList;",
                    outv:"SearchTodo_visible=showSearchTodo; EditTodoList_visible = showEditTodoList;"
            ],
            [where:"param"  , in:"\$selectEmployee", out:"\$scope.selectEmployee", outv:"selectEmployee" ],
            [where:"globals", in:"\$\$user"        , out:"\$scope._user",          outv:"_user"],

            [where:"value", in:"Complete \$EmployeeData.JOB forms for \$EmployeeData.ENAME",
                    out:"Complete \$scope.EmployeeData.JOB forms for \$scope.EmployeeData.ENAME",
                    outv:"Complete EmployeeData.JOB forms for EmployeeData.ENAME"
            ],
            [where:"value", in:"\$EmployeeDetails.selectedRecords",
                    out:"\$scope.EmployeeDetails.selectedRecords",
                    outv:"EmployeeDetails.selectedRecords"
            ],
    ]

    //Expressions to test new compileExpression method for targets CtrlFunction, DOMExpression
    def expressionsFunc = [
            [in:" \$var.property"   , out:" \$scope.var.property"        ],
            [in:"\$var.property"    , out:"\$scope.var.property"         ],
            [in:"\$\$var.\$property", out:"\$scope._var.\$property"      ],
            [in:"var.property"      , out:"var.property"                 ],
            [in:"\$var.\$visible"   , out:"\$scope.var_visible"          ],
            [in:"\$var.\$style"     , out:"\$scope.var_style"            ],
            [in:"\$var.\$property"  , out:"\$scope.var.\$property"       ],
            [in:"\$F1.\$dirty"    , out:"\$scope.F1.\$dirty"           ],

            [where:"onLoad",in:"if (\$EmployeeData.JOB =='MANAGER') \$\$activateFlow('managerFlow'); else \$\$activateFlow('employeeFlow');",
                    out:"if (\$scope.EmployeeData.JOB =='MANAGER') \$scope._activateFlow('managerFlow'); else \$scope._activateFlow('employeeFlow');",
            ],

            [where:"onUpdate",in:"\$EmployeeData.\$load();",
                    out:"\$scope.EmployeeDataDS.load();",
            ],
            [where:"onClick",in:"\$SearchTodo.\$visible=\$showSearchTodo; \$EditTodoList.\$visible = \$showEditTodoList;",
                    out:"\$scope.SearchTodo_visible=\$scope.showSearchTodo; \$scope.EditTodoList_visible = \$scope.showEditTodoList;",
            ],
            [where:"param"  , in:"\$selectEmployee", out:"\$scope.selectEmployee", outv:"selectEmployee" ],
            [where:"globals", in:"\$\$user"        , out:"\$scope._user",          outv:"_user"],

            [where:"value", in:"Complete \$EmployeeData.JOB forms for \$EmployeeData.ENAME",
                    out:"Complete \$scope.EmployeeData.JOB forms for \$scope.EmployeeData.ENAME",
            ],
            [where:"value", in:"\$EmployeeDetails.selectedRecords",
                    out:"\$scope.EmployeeDetails.selectedRecords",
            ],
    ]

    //Expressions to test new compileExpression method for targets DOMDisplay
    def expressionsDOMDisplay = [
            [in:"{{ \$var.property}}"                              , out:"{{ var.property}}" ],
            [in:"{{ \$var.property }} {{\$EmployeeData.JOB}}"      , out:"{{ var.property }} {{EmployeeData.JOB}}" ],
            [in:"{{\$EmployeeData.\$dirty}}"                       , out:"{{EmployeeDataDS.dirty()}}"              ],
            [in:"{{\$F1.\$dirty}}"                                 , out:"{{F1.\$dirty}}"                          ],
            [in:"{{\$\$eval(\$value1)}}"                            , out:"{{\$eval(value1)}}"                     ],
            [in:"\$var.property"                                   , out:"{{ var.property }}"                      ],
            [in:"\$var.\$style"                                    , out:"{{ var_style }}"                         ],
            [in:"\$var.\$visible"                                  , out:"{{ var_visible }}"                       ],
            [in:"\$var.\$property"                                 , out:"{{ var.\$property }}"                    ],
            [in:"\$\$var.property"                                 , out:"{{ _var.property }}"                     ],
            [in:"\$\$var.\$property"                               , out:"{{ _var.\$property }}"                   ],
            [in:"var.property"                                     , out:"var.property"                            ]

    ]
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCompileExpressionDOMDisplay() {
        expressionsDOMDisplay.each{
            def okIndicator = "---->:"
            def failIndicator = "-FAIL:"
            def result =  cs.compileExpression(it.in,CompileService.ExpressionTarget.DOMDisplay, ["EmployeeData"])
            def ok = result == it.out
            def test = ok ? okIndicator : failIndicator
            def expected = ok ?"" : "  <- Expected: "+it.out

            println "input: ${it.in} \n$test $result $expected"
            assert ok
        }
    }

    void testCompileExpressionCtrl() {
        expressionsFunc.each{
            def okIndicator = "---->:"
            def failIndicator = "-FAIL:"
            def result =  cs.compileExpression(it.in,CompileService.ExpressionTarget.CtrlFunction, ["EmployeeData"])
            def ok = result == it.out
            def test = ok ? okIndicator : failIndicator
            def expected = ok ?"" : "  <- Expected: "+it.out

            println "input: ${it.in} \n$test $result $expected"
            assert ok
        }
    }

    void testCompileExpressionDOMEvent() {
        expressionsFunc.each{
            def okIndicator = "---->:"
            def failIndicator = "-FAIL:"
            def shouldBe = it.out.replace("\$scope.","")
            def result =  cs.compileExpression(it.in,CompileService.ExpressionTarget.DOMExpression, ["EmployeeData"])
            def ok = result == shouldBe
            def test = ok ? okIndicator : failIndicator
            def expected = ok ?"" : "  <- Expected: "+shouldBe

            println "input: ${it.in} \n$test $result $expected"
            assert ok
        }
    }

    /* Usage parseExpression:
    onLoad -> postQuery for data set (compileService #246)  - no replacement for data sets is applied (bug?)
    onClick ->   $scope.<component>_onClick = function($arg) {$expr}; - post processed to replace .<ds>_ with .$DS. (compileService #457)
    onUpdate ->  $scope.<component>_onUpdate = function(..) { $expr;}    for ng-change
        where component is $name or $parent_$name - post processed to replace .<ds>_ with .$DS. (compileService #480)


    parseOnEventFunction(expr, component):
        starts with replacing .$<f> with DS.<t> [ [f:"populateSource",t:"load"],[f:"load", t:"load"], [f:"get", t:"get"]]
        used to generate onUpdate for dataSet based components (radio, select)

    in compileService.normalizeComponent
        parse parameters (compileService #655,664)
        no pre or post processing (would be too early as data sets are not compiled yet)

    in PageComponent.initNewRecordJS()  - post processed to replace .<ds>_ with .$DS.

    */

    void XtestParseExpression() {
        expressions.each{
            def okIndicator = "---->:"
            def failIndicator = "-FAIL:"
            def result =  cs.parseExpression(it.in)
            def test = result == it.out? okIndicator : failIndicator
            println "input: ${it.in} \n$test $result "
            //assert result == it.out
        }
    }

    /* Usage parseVariable: (for code put directly in the DOM e.g. ng-submit)
    submit -> submit for Form ng-submit.   !not post processed, a bug?
    value -> expression to set default value using ng-init. Post processed to replace .<ds>_ with .$DS.
    value -> expression to render display type as HTML for items not bound to parent data set.  !not post processed, a bug?
    value -> expression to set input value attribute. !not post processed, a bug?

    In principle, parse variable does the same as parse expression, except that the latter prefixes variables with $scope.
    */
    void XtestParseVariable() {
        expressions.each{
            def okIndicator = "---->:"
            def failIndicator = "-FAIL:"
            def result =  cs.parseVariable(it.in)
            def test = result == it.outv? okIndicator : failIndicator
            println "input: ${it.in} \n$test $result "
            //assert result == it.outv
        }
    }

    /* Usage parseLiteral: (for expressions to be rendered in the dom)
    value -> expression for rendering grid literal
    value -> expression for rendering literal in other controls
    value -> expression for rendering display in non-grid controls
    url   -> href expression for links in non-grid controls
     */
    void XtestParseLiteral() {
        expressions.each{
            def okIndicator = "---->:"
            def failIndicator = "-FAIL:"
            def result =  cs.parseLiteral(it.in)
            def test = result == it.out? okIndicator : failIndicator
            println "input: ${it.in} \n$test $result "
            //assert result == it.out
        }
    }


}
