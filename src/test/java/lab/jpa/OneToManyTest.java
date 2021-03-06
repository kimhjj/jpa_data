package lab.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import lab.jpa.domain.PDSBoard;
import lab.jpa.domain.PDSFile;
import lab.jpa.persistence.PDSBoardRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneToManyTest {
	@Autowired
	PDSBoardRepository repo;
	
	@Test
	public void testInsertPDS() {
		PDSBoard pds = new PDSBoard();
		pds.setPname("DOCUMNET 1 -2");
	
		PDSFile file1 = new PDSFile();
		file1.setPdsfile("file1.doc");
		PDSFile file2 = new PDSFile();
		file2.setPdsfile("file2.doc");
		
		pds.setFiles(Arrays.asList(file1, file2));
		System.out.println("try to save pds");
	
		repo.save(pds);
	}
	
	@Test
	public void insertDummies() {
		List<PDSBoard> list = new ArrayList<>();
		
		IntStream.range(1, 100).forEach(i -> {
			PDSBoard pds = new PDSBoard();
			pds.setPname("자료 " +i);

			PDSFile file1 = new PDSFile();
			file1.setPdsfile("file1.doc");
			PDSFile file2 = new PDSFile();
			file2.setPdsfile("file2.doc");

			pds.setFiles(Arrays.asList(file1, file2));
			System.out.println("try to save pds");
			list.add(pds);
		});
		
		repo.saveAll(list);
	}
	
	@Transactional
	@Commit
	@Test
	public void testUpdateFileName1() {
		Long fno = 1L;
		String newName = "updatedFile1.doc";
		int count = repo.updatePDSFile(fno, newName);
		System.out.println("update count: " + count);
	}

	@Transactional
	@Commit
	@Test
	public void testUpdateFileName2() {
		String newName = "updatedFile2.doc";
		Optional<PDSBoard> result = repo.findById(2L);
		result.ifPresent(pds -> {
			System.out.println("데이터가 존재하므로 update 시도");
			
			PDSFile target = new PDSFile();
			target.setFno(3L);	// pds.getFiles()
			target.setPdsfile(newName);

			int idx = pds.getFiles().indexOf(target);
			System.out.println(idx);
			
			if (idx > -1) {
				List<PDSFile> list = pds.getFiles();
				list.remove(idx);
				list.add(target);
				pds.setFiles(list);
			}
			
			repo.save(pds);
		});
	}

	@Transactional
	@Commit
	@Test
	public void deletePDSFile() {
		Long fno = 2L;
		int count = repo.deletePDSFile(fno);
		System.out.println("DELETE PDSFILE: " + count);
	}
}
