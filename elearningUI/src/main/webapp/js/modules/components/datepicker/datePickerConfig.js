define(["ko", "jquery", "jquery-ui"], function (ko, $) {
    ko.bindingHandlers.datepicker = {
        init: function(element, valueAccessor, allBindingsAccessor) {
            let $el = $(element);

            //initialize datepicker with some optional options
            let options = allBindingsAccessor().datepickerOptions || {};
            $el.datepicker(options);

            //handle the field changing
            ko.utils.registerEventHandler(element, "change", function() {
                let observable = valueAccessor();
                observable($el.datepicker("getDate"));
            });

            //handle disposal (if KO removes by the template binding)
            ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
                $el.datepicker("destroy");
            });

        },
        update: function(element, valueAccessor) {
            let value = ko.utils.unwrapObservable(valueAccessor()),
                $el = $(element),
                current = $el.datepicker("getDate");

            if (value - current !== 0) {
                $el.datepicker("setDate", value);
            }
        }
    };

    ko.bindingHandlers.fileUpload = {
        init: function (element, valueAccessor) {
            $(element).change(function () {
                valueAccessor()(element.files[0]);
            });
        },
        update: function (element, valueAccessor) {
            if (ko.unwrap(valueAccessor()) === null) {
                $(element).wrap('<form>').closest('form').get(0).reset();
                $(element).unwrap();
            }
        }
    };
});

