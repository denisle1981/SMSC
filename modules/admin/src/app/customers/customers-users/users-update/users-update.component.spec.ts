import {TestBed, async, inject} from "@angular/core/testing";
import {TranslateModule} from "ng2-translate";
import {RouterTestingModule} from "@angular/router/testing";
import {MockBackend} from "@angular/http/testing";
import {XHRBackend, Response, ResponseOptions} from "@angular/http";
import {OneToManyComponent, OneToManyModule} from "./one-to-many.component";
import {ComponentHelper} from "../../../shared/component-fixture";
import {CustomersContactsModule} from "../customers-contacts.module";
import {APP_PROVIDERS} from "../../../app.module";
import {ContactsUpdateComponent} from "./contacts-update.component";
import {ConfigService} from "../../../config/config.service";
import {ConfigServiceMock} from "../../../shared/test/stub/config.service";
import {UsersUpdateComponent} from "./users-update.component";
import {UsersModule} from "../customers-users.module";
import {CustomerUser} from "../../model/customer-user";

describe('Component: UsersUpdateComponent', () => {
    let componentFixture: ComponentHelper<UsersUpdateComponent> =
        new ComponentHelper<UsersUpdateComponent>(null, null, null, null);
    let mockBackend;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [UsersModule, RouterTestingModule, TranslateModule.forRoot()],
            providers: [
                APP_PROVIDERS,
                {provide: XHRBackend, useClass: MockBackend},
                {provide: ConfigService, useClass: ConfigServiceMock},
            ]
        });

        componentFixture.fixture = TestBed.createComponent(UsersUpdateComponent);
        componentFixture.instance = componentFixture.fixture.componentInstance;
        componentFixture.element = componentFixture.fixture.nativeElement;
        componentFixture.debugElement = componentFixture.fixture.debugElement;

        componentFixture.instance.entity = <CustomerUser>{
            _links: {
                self: {
                    href: ''
                }
            }
        };
    });

    beforeEach(inject([XHRBackend], (_mockBackend) => {
        mockBackend = _mockBackend;
    }));

    it('should have the `users-update-form`', async(() => {
        componentFixture.fixture.detectChanges();
        componentFixture.fixture.whenStable().then(() => {
            expect(componentFixture.element.querySelector('#users-update-form')).toBeDefined();
        });
    }));

    it('should get resources', async(() => {
        let data: CustomerUser = <CustomerUser>{
            firstname: 'firstname',
            _links: {
                self: {
                    href: ''
                }
            }
        };

        mockBackend.connections.subscribe(connection => {
            let response = new ResponseOptions({body: data});
            connection.mockRespond(new Response(response));
        });

        componentFixture.instance.onSubmit(data)
            .subscribe(res => {
                expect(res.firstname).toEqual('firstname');
            });
    }));
});
