package net.hedtech.banner.sspb

class ParseTests extends GroovyTestCase  {
    def page = new PageComponent()


    //Expressions to test new compileExpression method for targets CtrlFunction, DOMExpression
    def expressionsFunc = [
            [in:"\$todoResource.\$post({page:'p'})", out:"\$scope.todoResource.post({page:'p'})" ],
            [in:"\$todoResource.\$put({page:'p'})" , out:"\$scope.todoResource.put({page:'p'})" ],
            [in:" \$var.property"                  , out:" \$scope.var.property"        ],
            [in:"\$var.property"                   , out:"\$scope.var.property"         ],
            [in:"\$\$var.\$property"               , out:"\$scope._var.\$property"      ],
            [in:"var.property"                     , out:"var.property"                 ],
            [in:"\$var.\$visible"                  , out:"\$scope.var_visible"          ],
            [in:"\$var.\$style"                    , out:"\$scope.var_style"            ],
            [in:"\$var.\$property"                 , out:"\$scope.var.\$property"       ],
            [in:"\$F1.\$dirty"                     , out:"\$scope.F1.\$dirty"           ],
            [in:"\$EmployeeData.\$data"            , out:"\$scope.EmployeeDataDS.data"    ],
            [in:"\$EmployeeData.\$save"            , out:"\$scope.EmployeeDataDS.save"    ],
            [in:"\$EmployeeData.\$load"            , out:"\$scope.EmployeeDataDS.load"    ],
            //[in:"\$EmployeeData.\$currentRecord"   , out:"\$scope.EmployeeDataDS.currentRecord"    ],
            [in:"\$EmployeeData.\$selection"       , out:"\$scope.EmployeeDataDS.selectedRecords"    ],
            [where:"onLoad",in:"if (\$EmployeeData.JOB =='MANAGER') \$\$activateFlow('managerFlow'); else \$\$activateFlow('employeeFlow');",
                           out:"if (\$scope.EmployeeData.JOB =='MANAGER') \$scope._activateFlow('managerFlow'); else \$scope._activateFlow('employeeFlow');",
            ],
            [where:"onClick",in:"\$SearchTodo.\$visible=\$showSearchTodo; \$EditTodoList.\$visible = \$showEditTodoList;",
                            out:"\$scope.SearchTodo_visible=\$scope.showSearchTodo; \$scope.EditTodoList_visible = \$scope.showEditTodoList;",
            ],
            [where:"param"  , in:"\$selectEmployee", out:"\$scope.selectEmployee"],
            [where:"globals", in:"\$\$user"        , out:"\$scope._user"],

            [where:"value", in:"Complete \$EmployeeData.JOB forms for \$EmployeeData.ENAME",
                           out:"Complete \$scope.EmployeeData.JOB forms for \$scope.EmployeeData.ENAME",
            ],
            [where:"value", in:"\$EmployeeDetails.selectedRecords",
                           out:"\$scope.EmployeeDetails.selectedRecords",
            ]
    ]

    //Expressions to test new compileExpression method for targets DOMDisplay
    def expressionsDOMDisplay = [


            [in:"Input with pattern ^\\w+\$"                       , out:"Input with pattern ^\\w+\$" ],
            [in:"{{ \$var.property}}"                              , out:"{{ var.property}}" ],
            [in:"{{ \$var.property }} {{\$EmployeeData.JOB}}"      , out:"{{ var.property }} {{EmployeeData.JOB}}" ],
            [in:"{{\$EmployeeData.\$dirty}}"                       , out:"{{EmployeeDataDS.dirty()}}"              ],
            [in:"{{\$F1.\$dirty}}"                                 , out:"{{F1.\$dirty}}"                          ],
            [in:"{{\$\$eval(\$value1)}}"                           , out:"{{\$eval(value1)}}"                      ],
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
            def result =  page.compileExpression(it.in,PageComponent.ExpressionTarget.DOMDisplay, ["EmployeeData"], ["todoResource"])
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
            def result =  page.compileExpression(it.in,PageComponent.ExpressionTarget.CtrlFunction, ["EmployeeData"], ["todoResource"])
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
            def result =  page.compileExpression(it.in,PageComponent.ExpressionTarget.DOMExpression, ["EmployeeData"], ["todoResource"])
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




}
