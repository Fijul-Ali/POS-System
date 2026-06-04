package com.ust.pos;

import com.ust.pos.dto.NodeDto;
import com.ust.pos.model.Node;
import com.ust.pos.model.NodeRepository;
import com.ust.pos.model.User;
import com.ust.pos.model.UserRepository;
import com.ust.pos.node.service.impl.NodeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class NodeServiceTest {

    @Mock
    private NodeRepository nodeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NodeServiceImpl nodeService;

    @Mock
    private UserRepository userRepository;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        NodeDto nodeDto = new NodeDto();
        nodeDto.setIdentifier("Admin");

        Node node = new Node();
        Mockito.when(modelMapper.map(nodeDto, Node.class)).thenReturn(node);
        Mockito.when(nodeRepository.save(node)).thenReturn(node);

        NodeDto response = nodeService.save(nodeDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        NodeDto nodeDto = new NodeDto();
        nodeDto.setIdentifier("Admin");

        Node existingNode = new Node();
        existingNode.setIdentifier("Admin");
        Mockito.when(nodeRepository.findByIdentifier("Admin")).thenReturn(existingNode);

        NodeDto response = nodeService.save(nodeDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Node node = new Node();
        node.setIdentifier("Admin");

        NodeDto nodeDto = new NodeDto();
        nodeDto.setIdentifier("Admin");

        Mockito.when(nodeRepository.findByIdentifier("Admin")).thenReturn(node);
        Mockito.when(modelMapper.map(node, NodeDto.class)).thenReturn(nodeDto);

        NodeDto response = nodeService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        NodeDto nodeDto = new NodeDto();
        nodeDto.setId(1L);

        Node node = new Node();
        Mockito.when(nodeRepository.findById(1L)).thenReturn(Optional.of(node));

        NodeDto response = nodeService.update(nodeDto);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void updateTestNotFound() {
        NodeDto nodeDto = new NodeDto();
        nodeDto.setId(1L);

        Mockito.when(nodeRepository.findById(1L)).thenReturn(Optional.empty());

        NodeDto response = nodeService.update(nodeDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = nodeService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Node node = new Node();
        node.setIdentifier("Admin");

        NodeDto nodeDto = new NodeDto();
        nodeDto.setIdentifier("Admin");

        List<Node> nodes = List.of(node);
        List<NodeDto> nodeDtos = List.of(nodeDto);

        Page<Node> nodePage = new PageImpl<>(nodes, PageRequest.of(0, 2), nodes.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));

        Mockito.when(nodeRepository.findAll(pageable)).thenReturn(nodePage);
        Mockito.when(modelMapper.map(Mockito.eq(nodes), Mockito.any(java.lang.reflect.Type.class))).thenReturn(nodeDtos);

        List<NodeDto> response = nodeService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        NodeDto nodeDto = new NodeDto();
        nodeDto.setIdentifier("Admin");
        nodeDto.setStatus(true);

        Node node = new Node();
        Mockito.when(nodeRepository.findByIdentifier("Admin")).thenReturn(node);
        Mockito.when(modelMapper.map(node, NodeDto.class)).thenReturn(nodeDto);
        NodeDto response = nodeService.updateStatus("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Node node = new Node();
        node.setIdentifier("Admin");

        NodeDto nodeDto = new NodeDto();
        nodeDto.setIdentifier("Admin");

        List<Node> nodes = List.of(node);
        List<NodeDto> nodeDtos = List.of(nodeDto);

        Mockito.when(nodeRepository.findByStatus(true)).thenReturn(nodes);
        Mockito.when(modelMapper.map(Mockito.eq(nodes), Mockito.any(java.lang.reflect.Type.class))).thenReturn(nodeDtos);

        List<NodeDto> response = nodeService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void getNodesForRolesTest() {
        // Mock security context
        org.springframework.security.core.userdetails.User principal =
                new org.springframework.security.core.userdetails.User(
                        "admin@test.com", "password", List.of());

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(principal);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock user with roles
        User user = new User();
        user.setRoles(List.of("ADMIN"));
        Mockito.when(userRepository.findByUsername("admin@test.com")).thenReturn(user);

        // Mock nodes
        Node node = new Node();
        node.setIdentifier("dashboard");
        node.setRoles(List.of("ADMIN"));
        Mockito.when(nodeRepository.findAll()).thenReturn(List.of(node));

        // Mock finding node by identifier
        NodeDto nodeDto = new NodeDto();
        nodeDto.setIdentifier("dashboard");
        Mockito.when(nodeRepository.findByIdentifier("dashboard")).thenReturn(node);
        Mockito.when(modelMapper.map(node, NodeDto.class)).thenReturn(nodeDto);

        List<NodeDto> response = nodeService.getNodesForRoles();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
    }

    @Test
    void getNodesForRolesTest_NoAuth() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        List<NodeDto> response = nodeService.getNodesForRoles();

        Assertions.assertTrue(response.isEmpty());
    }
}