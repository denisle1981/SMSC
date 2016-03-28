import {Directive, ElementRef} from 'angular2/core';

@Directive({
    selector: '[prices-grid]'
})
export class PricesGrid {
    private static visible = false;
    private static priceStore: any;

    constructor(private element: ElementRef) {}

    mainStore() {
        return Ext.create('Ext.data.Store', {
            model: 'Prices',
            data: [
                {mcc: '1', mnc: '2', price: '3', type: 'numeric', valid_from: '10/10/2014', valid_to: '10/10/2014'}
            ]
        });
    }

    ngOnInit() {
        if (!PricesGrid.visible) {
            Ext.define('Prices', {
                extend: 'Ext.data.Model',
                fields: [
                    {name: 'mcc', type: 'number'},
                    {name: 'mnc', type: 'number'},
                    {name: 'price', type: 'number'},
                    {name: 'type', type: 'string'},
                    {name: 'valid_from', type: 'date', dateFormat: 'n/j/Y'},
                    {name: 'valid_to', type: 'date', dateFormat: 'n/j/Y'}
                ]
            });

            PricesGrid.priceStore = this.mainStore();
        }

        var enumType = Ext.create('Ext.data.Store', {
            fields: ['abbr', 'name'],
            data: [
                {"abbr": "numeric", "name": "numeric"},
                {"abbr": "alphanumeric", "name": "alphanumeric"},
                {"abbr": "dynamic", "name": "dynamic"}
            ]
        });

        var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
            clicksToMoveEditor: 1,
            autoCancel: false
        });

        let grid = Ext.create('Ext.grid.Panel', {
            renderTo: this.element.nativeElement,
            store: PricesGrid.priceStore,
            width: 1000,
            height: 500,
            title: 'Gateway',
            columns: [
                {
                    text: 'MCC',
                    dataIndex: 'mcc',
                    flex: 1,
                    editor: {
                        allowBlank: false
                    }
                },
                {
                    text: 'MNC',
                    dataIndex: 'mnc',
                    flex: 1,
                    editor: {
                        allowBlank: false
                    }
                },
                {
                    text: 'Price',
                    flex: 1,
                    dataIndex: 'price',
                    editor: {
                        allowBlank: false
                    }
                },
                {
                    dataIndex: 'type',
                    xtype: 'gridcolumn',
                    text: 'Type',
                    flex: 1,
                    editor: {
                        xtype: 'combobox',
                        displayField: 'name',
                        valueField: 'abbr',
                        queryMode: 'remote',
                        store: enumType
                    }
                },
                {
                    xtype: 'datecolumn',
                    header: 'Valid from',
                    dataIndex: 'valid_from',
                    width: 135,
                    editor: {
                        xtype: 'datefield',
                        allowBlank: false,
                        format: 'm/d/Y',
                        minValue: '01/01/2006',
                        minText: 'Cannot have a start date before the company existed!',
                        maxValue: Ext.Date.format(new Date(), 'm/d/Y')
                    }
                },
                {
                    xtype: 'datecolumn',
                    header: 'Valid to',
                    dataIndex: 'valid_to',
                    width: 135,
                    editor: {
                        xtype: 'datefield',
                        allowBlank: false,
                        format: 'm/d/Y',
                        minValue: '01/01/2006',
                        minText: 'Cannot have a start date before the company existed!',
                        maxValue: Ext.Date.format(new Date(), 'm/d/Y')
                    }
                },
            ],
            tbar: [{
                text: 'Add',
                handler: () => {
                    rowEditing.cancelEdit();

                    // Create a model instance
                    var r = Ext.create('Prices', {
                        mcc: '1',
                        mnc: '2',
                        price: '500',
                        type: 'numeric',
                        valid_from: '10/10/2014',
                        valid_to: '10/10/2014'
                    });

                    PricesGrid.priceStore.insert(0, r);
                    rowEditing.startEdit(0, 0);
                }
            }, {
                itemId: 'removeEmployee',
                text: 'Remove',
                handler: () => {
                    var sm = grid.getSelectionModel();
                    rowEditing.cancelEdit();
                    PricesGrid.priceStore.remove(sm.getSelection());
                    if (PricesGrid.priceStore.getCount() > 0) {
                        sm.select(0);
                    }
                },
                disabled: true
            }],
            plugins: [rowEditing],
            listeners: {
                'selectionchange': (view, records) => {
                    grid.down('#removeEmployee').setDisabled(!records.length);
                }
            }
        });
        PricesGrid.visible = true;
    }
}
