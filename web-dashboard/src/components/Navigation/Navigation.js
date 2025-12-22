import "./Navigation.scss";
import { NavLink, useLocation } from 'react-router-dom';
import { Navbar, Nav, Dropdown, Container, Offcanvas } from "react-bootstrap";
import bell from '../../static/bell.png';

const Navigation = ({ show, setShow }) => {
    const location = useLocation();
    if (location.pathname === '/login') return null;

    return (
        <div className="navbar-header">
            <Navbar bg="white" className="nav-header px-2">
                <Container fluid className="d-flex justify-content-between align-items-center">
                    {!show && (
                        <span style={{ cursor: "pointer" }} onClick={() => setShow(true)}>
                            <i className="fa fa-bars" style={{ fontSize: "20px" }}></i>
                        </span>
                    )}
                    <Offcanvas 
                        show={show} 
                        onHide={() => setShow(false)}
                        backdrop={window.innerWidth < 992}
                        scroll={true}
                        className="custom-sidebar"
                    >
                        <div className="sidebar-header-custom d-flex justify-content-between align-items-center p-4">
                            <h4 className="text-white fw-bold mb-0">Smart Farm</h4>
                            <span className="close-btn" onClick={() => setShow(false)}>
                                <i className="fa fa-times-circle fa-lg"></i>
                            </span>
                        </div>

                        <Offcanvas.Body className="px-3">
                            <Nav className="flex-column">
                                <NavLink to="/" exact className="nav-link-item">
                                    <i className="fa fa-th-large me-2"></i> Dashboard
                                </NavLink>
                                <NavLink to="/setting" className="nav-link-item">
                                    <i className="fa fa-cog me-2"></i> Setting
                                </NavLink>
                                <NavLink to="/profile" className="nav-link-item">
                                    <i className="fa fa-user me-2"></i> Profile
                                </NavLink>
                                <NavLink to="/login" className="nav-link-item">
                                    <i className="fa fa-sign-out me-2"></i> Logout
                                </NavLink>
                            </Nav>
                        </Offcanvas.Body>
                    </Offcanvas>

                    <Nav className="ms-auto d-flex align-items-center">
                        <NavLink to="/notification" className="nav-link me-3">
                            <img src={bell} style={{ width: "24px" }} alt="Notification" />
                        </NavLink>
                        <Dropdown>
                            <Dropdown.Toggle as="a" className="nav-link p-0">
                                <i className="fa fa-user-circle fa-lg"></i>
                            </Dropdown.Toggle>
                            <Dropdown.Menu align="end">
                                <Dropdown.Item as={NavLink} to="/profile">Profile</Dropdown.Item>
                                <Dropdown.Item as={NavLink} to="/login">Logout</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                    </Nav>
                </Container>
            </Navbar>
        </div>
    );
};

export default Navigation;